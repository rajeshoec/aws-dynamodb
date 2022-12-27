package com.techgeeknext.controller;

import com.techgeeknext.entity.Customer;
import com.techgeeknext.entity.Order;
import com.techgeeknext.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/add/customer")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerRepository.saveCustomer(customer);
    }

    @GetMapping("/get/customer/{id}")
    public Customer getCustomerById(@PathVariable("id") String customerId) {
        return customerRepository.getCustomerById(customerId);
    }

    @DeleteMapping("/delete/customer/{id}")
    public String deleteCustomerById(@PathVariable("id") String customerId) {
        return  customerRepository.deleteCustomerById(customerId);
    }

    @PutMapping("/update/customer/{id}")
    public String updateCustomer(@PathVariable("id") String customerId, @RequestBody Customer customer) {
        return customerRepository.updateCustomer(customerId,customer);
    }

    @GetMapping("/get/orderDetails")
    public List<Order> getOrder(@RequestParam String customerId,@RequestParam(required = false) String searchWord,
                                @RequestParam(required = false) String orderStatus,@RequestParam String from, @RequestParam String to, @RequestParam int page, @RequestParam int size) {
        System.out.println("=====Start Time : "+System.currentTimeMillis());
        long startTime = System.currentTimeMillis();
        List<Order> orders = null;
        orders = customerRepository.getOrdersWithQuery(customerId,searchWord,orderStatus,from,to,page,size);
        System.out.println(System.currentTimeMillis() - startTime);
        return orders;
    }

    @GetMapping("/get/orderDetailsWithScan")
    public List<Order> getOrderWithScan(@RequestParam String customerId,@RequestParam(required = false) String searchWord,
                                        @RequestParam(required = false) String orderStatus,@RequestParam String from, @RequestParam String to, @RequestParam int page, @RequestParam int size) {
        System.out.println("=====Start Time : "+System.currentTimeMillis());
        long startTime = System.currentTimeMillis();
        List<Order> orders = null;
        orders = customerRepository.getOrdersWithScan(customerId,searchWord,orderStatus,from,to,page,size);
        System.out.println(System.currentTimeMillis() - startTime);
        return orders;
    }
}
