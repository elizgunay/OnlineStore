package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerOrderCountDTO;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;

public interface OrderService {

    //Admin
    List<Order> findAllOrders();

    // Customer

    Order saveOrder(ShoppingCart cart);

    void acceptOrder(Long id);

    void cancelOrder(Long id);

    long getOrderCount();

    long getProductCount();
    List<CustomerOrderCountDTO> getCustomerOrderCounts();
}

