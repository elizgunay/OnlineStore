package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        try{
            Customer cusSave = new Customer(customer.getFirstName());
            return customerRepository.save(cusSave);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Customer> findByIdd(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer update(Customer customer) {
        Customer customerUpdate = customerRepository.getReferenceById(customer.getId());
        customerUpdate.setFirstName(customer.getFirstName());
        return customerRepository.save(customerUpdate);    }

    @Override
    public void deleteById(Long id) {
        Customer customer = customerRepository.getReferenceById(id);

        customer.set_deleted(true);
        customer.set_activated(false);
        customerRepository.save(customer);
    }


    @Override
    public void enabledById(Long id) {
        Customer customer = customerRepository.getById(id);
        customer.set_activated(true);
        customer.set_deleted(false);
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAllByActivated() {
        return customerRepository.findAllByActivated();
    }

    @Override
    public boolean existsByUsername(String username) {
        return customerRepository.existsByUsername(username);
    }

    //Customer
    @Override
    public CustomerDto save(CustomerDto customerDto) {

        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));

        Customer customerSave = customerRepository.save(customer);
        return mapperDto(customerSave);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }


    public CustomerDto mapperDto(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setUsername(customer.getUsername());
        return customerDto;
    }

    @Override
    public Customer saveInfo(Customer customer) {
        Customer customer1 = customerRepository.findByUsername(customer.getUsername());

        customer1.setAddress(customer.getAddress());
        customer1.setCity(customer.getCity());
        customer1.setCountry(customer.getCountry());
        customer1.setPhoneNumber(customer.getPhoneNumber());

        return customerRepository.save(customer1);
    }
}
