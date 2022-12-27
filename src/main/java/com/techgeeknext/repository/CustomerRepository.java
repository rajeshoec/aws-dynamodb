package com.techgeeknext.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.techgeeknext.entity.Customer;
import com.techgeeknext.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomerRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Order> getOrdersWithQuery(String customerId, String searchWord, String orderStatus, String startDate, String endDate, int page, int size) {
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":customerId", new AttributeValue().withS(customerId));
        attributeValues.put(":startDate", new AttributeValue().withS(startDate));
        attributeValues.put(":endDate", new AttributeValue().withS(endDate));

        StringBuilder filterExpression = new StringBuilder();

        addFilterCriteria(filterExpression, attributeValues, "orderDetails", searchWord, "contains(%s, :%s)");
        addFilterCriteria(filterExpression, attributeValues, "orderStatus", orderStatus, "%s = :%s");

        DynamoDBQueryExpression<Order> queryExpression = new DynamoDBQueryExpression<Order>();
        queryExpression.withIndexName("customerIdIndex");
        queryExpression.withKeyConditionExpression("customerId = :customerId and orderDate between :startDate and :endDate");
        if (!filterExpression.toString().isEmpty()) {
            queryExpression.withFilterExpression(filterExpression.toString());
        }
        queryExpression.withExpressionAttributeValues(attributeValues);
        queryExpression.setLimit(size);
//        queryExpression.withExclusiveStartKey(getExclusiveStartKey(page, customerId));
        queryExpression.withConsistentRead(false);

//        dynamoDBMapper.getDynamoDBClient()

        return dynamoDBMapper.query(Order.class, queryExpression);
    }

    private void addFilterCriteria(StringBuilder filterExpression, Map<String, AttributeValue> attributeValues, String attributeName, String attributeValue, String expressionFormat) {
        if (attributeValue != null) {
            attributeValues.put(":" + attributeName, new AttributeValue().withS(attributeValue));
            if (filterExpression.length() > 0) {
                filterExpression.append(" and ");
            }
            filterExpression.append(String.format(expressionFormat, attributeName, attributeName));
        }
    }

    private Map<String, AttributeValue> getExclusiveStartKey(int page, String customerId) {
        if (page <= 1) {
            return null;
        }

        Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
        exclusiveStartKey.put("customerId", new AttributeValue().withS(customerId));
//        exclusiveStartKey.put("orderDate", new AttributeValue().withS("your-order-date"));
        return exclusiveStartKey;
    }


    public List<Order> getOrdersWithScan(String customerId, String searchWord, String orderStatus, String startDate, String endDate, int page, int size) {

        Map<String, AttributeValue> attributeValues = new HashMap<>();
        StringBuilder filterExpression = new StringBuilder();
        attributeValues.put(":startDate", new AttributeValue().withS(startDate.toString()));
        attributeValues.put(":endDate", new AttributeValue().withS(endDate.toString()));

        filterExpression.append("orderDate BETWEEN :startDate AND :endDate");

        addFilterCriteria(filterExpression, attributeValues, "orderDetails", searchWord, "contains(%s, :%s)");
        addFilterCriteria(filterExpression, attributeValues, "orderStatus", orderStatus, "%s = :%s");
        addFilterCriteria(filterExpression, attributeValues, "customerId", customerId, "%s = :%s");
        DynamoDBScanExpression scanxpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression.toString())
                .withExpressionAttributeValues(attributeValues);

        return dynamoDBMapper.scan(Order.class, scanxpression);
    }

    public Customer saveCustomer(Customer customer) {
        dynamoDBMapper.save(customer);
        return customer;
    }

    public Customer getCustomerById(String customerId) {
        return dynamoDBMapper.load(Customer.class, customerId);
    }

    public String deleteCustomerById(String customerId) {
        dynamoDBMapper.delete(dynamoDBMapper.load(Customer.class, customerId));
        return "Customer Id : "+ customerId +" Deleted!";
    }

    public String updateCustomer(String customerId, Customer customer) {
        dynamoDBMapper.save(customer,
                new DynamoDBSaveExpression()
        .withExpectedEntry("customerId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(customerId)
                )));
        return customerId;
    }
}
