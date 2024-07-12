package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerOrderCountDTO;
import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.*;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ShoppingCartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order saveOrder(ShoppingCart cart) {
        Order order = new Order();
        order.setOrderStatus("PENDING");
        order.setOrderDate(new Date());
        order.setCustomer(cart.getCustomer());
        order.setTotalPrice(cart.getTotalPrices());
        order.setQuantity(cart.getQuantity());
        order.setPaymentMethod("Cash");

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(CartItem item: cart.getCartItem()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setProduct(item.getProduct());
            orderDetail.setUnitPrice(item.getProduct().getCostPrice());
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
            cartItemRepository.delete(item);
        }
        order.setOrderDetailList(orderDetailList);
        order.setOrderDetailList(orderDetailList);
        cart.setCartItem(new HashSet<>());
        cart.setTotalItems(0);
        cart.setTotalPrices(0);

        cartRepository.save(cart);
       return orderRepository.save(order);
    }

    @Override
    public void acceptOrder(Long id) {
        Order order = orderRepository.getReferenceById(id);
        order.setDeliveryDate(new Date());
        order.setOrderStatus("SHIPPING");
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public long getOrderCount() {
        return orderRepository.count();
    }

    @Override
    public long getProductCount() {
        return productRepository.count();
    }

    @Override
    public List<CustomerOrderCountDTO> getCustomerOrderCounts() {
        List<Object[]> results = orderRepository.countOrdersByCustomer();
        List<CustomerOrderCountDTO> dtoList = new ArrayList<>();

        for (Object[] result : results) {
            Long customerId = (Long) result[0];
            String customerName = (String) result[1];
            Long orderCount = (Long) result[2];

            CustomerOrderCountDTO dto = new CustomerOrderCountDTO();
            dto.setCustomerId(customerId);
            dto.setCustomerName(customerName);
            dto.setOrderCount(orderCount);


            dtoList.add(dto);
        }

        return dtoList;
    }
}
