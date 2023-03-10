package com.techgeeknext.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "store")
public class Order {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String orderId;

    @DynamoDBIndexHashKey(attributeName = "customerId", globalSecondaryIndexName = "customerIdIndex")
    private String customerId;

    @DynamoDBAttribute
    private String orderDate;

    @DynamoDBAttribute
    private String orderStatus;

    @DynamoDBAttribute
    private String orderDetails;

    @DynamoDBAttribute
    private String totalAmount;
}
