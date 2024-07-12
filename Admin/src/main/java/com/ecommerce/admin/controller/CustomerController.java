package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String customers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("size", customers.size());

        model.addAttribute("title", "Customer");
        model.addAttribute("customerNew", new Customer());
        return "customers";
    }

    @PostMapping("/add-customer")
    public String add(@ModelAttribute("customerNew") Customer customer, Model model, RedirectAttributes attributes) {

        if (!customerService.existsByUsername(customer.getUsername())) {
            customerService.saveCustomer(customer);
            attributes.addFlashAttribute("success", "Customer added successfully!");
        }else {
            attributes.addFlashAttribute("failed", "Failed to add because duplicate name");
        }

        return "redirect:/customers";
    }

    @RequestMapping(value = "/findByIdd", method = {RequestMethod.PUT, RequestMethod.GET})
    @ResponseBody
    public Optional<Customer> findByIdd(Long id) {
        return customerService.findByIdd(id);
    }

    @GetMapping("/update-customer")
    public String update(Customer customer, RedirectAttributes attributes) {
        try {
            customerService.update(customer);
            attributes.addFlashAttribute("success", "Updated Successfully!");

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to update because duplicate name");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error server");
        }

        return "redirect:/customers";
    }

    @RequestMapping(value = "/delete-customer", method = {RequestMethod.PUT, RequestMethod.GET})
    public String delete(Long id, RedirectAttributes attributes) {
        try {
            customerService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to deleted");
        }

        return "redirect:/customers";
    }


    @GetMapping("/deleteID-customer/{id}")
    public String deleteItem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        customerService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Successfully deleted!");
        return "redirect:/customers";
    }

    @RequestMapping(value = "/enable-customer", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id, RedirectAttributes attributes) {

        try {
            customerService.enabledById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to enable!");
        }
        return "redirect:/customers";

    }

}
