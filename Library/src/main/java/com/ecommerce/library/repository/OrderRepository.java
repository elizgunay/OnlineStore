package com.ecommerce.library.repository;

import com.ecommerce.library.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o.customer.id,o.customer.firstName, COUNT(o) FROM Order o GROUP BY o.customer.id")
    List<Object[]> countOrdersByCustomer();
}
