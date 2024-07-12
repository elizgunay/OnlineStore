package com.ecommerce.customer.controller;

import com.ecommerce.library.model.*;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
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
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCartService cartService;


    @GetMapping("/checkout")
    public String checkOut(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        model.addAttribute("title", "Profile");
        model.addAttribute("page", "Profile");


        if (customer.getAddress() == null || customer.getCity() == null || customer.getPhoneNumber() == null) {
            model.addAttribute("error","You must fill the information after checkout!");
            model.addAttribute("customer",customer);
            model.addAttribute("title", "Profile");
            model.addAttribute("page", "Profile");
            return "account";
        }else{
            ShoppingCart cart = customerService.findByUsername(principal.getName()).getShoppingCart();
            model.addAttribute("customer", customer);
            model.addAttribute("title", "Check-Out");
            model.addAttribute("page", "Check-Out");
            model.addAttribute("cart", cart);
            model.addAttribute("subTotal", cart.getTotalItems());
            return "checkout";
        }

    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, RedirectAttributes attributes) {
        orderService.cancelOrder(id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/order";
    }

    @GetMapping("/order")
    public String order(Principal principal,Model model){
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);

        List<Order> orderList = customer.getOrders();
        model.addAttribute("orders",orderList);
        model.addAttribute("title", "Order");
        model.addAttribute("page", "Order");
        return "order";
    }


    @GetMapping("/save-order")
    public String saveOrder(Principal principal,Model model){
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);


        ShoppingCart cart = customer.getShoppingCart();



        List<Order> orderList = customer.getOrders();
        model.addAttribute("orders",orderList);
        model.addAttribute("title", "Order Detail");
        model.addAttribute("page", "Order Detail");
        model.addAttribute("success", "Add order successfully");

        orderService.saveOrder(cart);
        return "redirect:/order";
    }
}
