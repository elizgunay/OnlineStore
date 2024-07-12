package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.CustomerOrderCountDTO;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("/index")
    public String index(Model model) {
        long orderCount = orderService.getOrderCount();
        List<Order> orders = orderService.findAllOrders();

        long productCount = orderService.getProductCount();
        List<Product> products = productService.getAllProducts();


        List<CustomerOrderCountDTO> customerOrderCounts = orderService.getCustomerOrderCounts();
        model.addAttribute("customerOrderCounts", customerOrderCounts);


        model.addAttribute("orderCount", orderCount);
        model.addAttribute("orders", orders);

        model.addAttribute("productCount",productCount);
        model.addAttribute("products",products);



        return "/index";
    }


    @GetMapping("/orders")
    public String getAll(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<Order> orderList = orderService.findAllOrders();
            model.addAttribute("orders", orderList);
            return "orders";
        }
    }

    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Long id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Order Accepted");
            return "redirect:/orders";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.cancelOrder(id);
            return "redirect:/orders";
        }
    }


}
