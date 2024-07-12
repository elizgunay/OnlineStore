package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    //Admin
    List<Customer> findAll();
    Customer saveCustomer(Customer customer);
    Optional<Customer> findByIdd(Long id);
    Customer update(Customer customer);
    void deleteById(Long id);
    void enabledById(Long id);

    List<Customer> findAllByActivated();
    boolean existsByUsername(String username);

    // Customer
    CustomerDto save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer saveInfo(Customer customer);
}
