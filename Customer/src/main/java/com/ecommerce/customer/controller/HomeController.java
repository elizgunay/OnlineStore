package com.ecommerce.customer.controller;


import com.ecommerce.library.dto.BrandDto;
import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService cart;

    @RequestMapping(value ={ "/","/index"},method = RequestMethod.GET)
    public String home(@RequestParam(name = "categoryFilter", required = false) String categoryFilter, Model model, Principal principal, HttpSession session){

        model.addAttribute("title", "Home");
        model.addAttribute("page", "Home");

        List<Product> products = productService.findTop5ByOrderByIdDescWithLimit();

        model.addAttribute("products",products);


        if (principal != null) {
            Customer customer = customerService.findByUsername(principal.getName());
            session.setAttribute("username", customer.getFirstName() + " " + customer.getLastName());
            ShoppingCart shoppingCart = customer.getShoppingCart();
            if (shoppingCart != null) {
                session.setAttribute("totalItems", shoppingCart.getTotalItems());
            }
        }
        return "home";
    }


    @GetMapping("/home")
    public String index(Model model){
        List<Category> categories = categoryService.findAll();
        List<Brand> brands = brandService.findAll();

        List<ProductDto> productDtos = productService.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("brands",brands);

        model.addAttribute("products",productDtos);
        return "index";
    }


    @GetMapping("/contact-us")
    public String contact(Model model){
        model.addAttribute("title", "Contact");
        model.addAttribute("page", "Contact");
        return "contact-us";
    }
}
