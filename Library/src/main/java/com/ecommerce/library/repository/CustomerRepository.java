package com.ecommerce.library.repository;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    // Admin
    boolean existsByUsername(String username);

    @Query("select c from Customer c where c.is_activated = true and c.is_deleted = false")
    List<Customer> findAllByActivated();

    //Customer

    Customer findByUsername(String username);
}
