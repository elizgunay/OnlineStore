package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title", "Login");
        model.addAttribute("page", "Login");

        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("customerDto",new CustomerDto());
        model.addAttribute("title", "Register");
        model.addAttribute("page", "Register");

        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDto")CustomerDto customerDto,
                                  BindingResult result,
                                  Model model){

       try{
           if(result.hasErrors()){
               model.addAttribute("customerDto",customerDto);
               return "register";
           }
           Customer customer = customerService.findByUsername(customerDto.getUsername());
           if(customer != null){
               model.addAttribute("username","Username have been registered");
               model.addAttribute("customerDto",customerDto);
               return "register";
           }

           if (customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
               customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
               customerService.save(customerDto);
               model.addAttribute("success", "Register successfully!");
           } else {
               model.addAttribute("error", "Password is not same");
               model.addAttribute("customerDto", customerDto);
               return "register";
           }


       }catch (Exception e){
           e.printStackTrace();
           model.addAttribute("error", "Server is error, try again later!");
       }

        return "register";
    }


}
