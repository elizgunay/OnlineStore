package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Brand;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.BrandService;
import com.ecommerce.library.service.CategoryService;
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
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/brands")
    public String brands(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        List<Brand> brands = brandService.findAll();
        model.addAttribute("brands", brands);
        model.addAttribute("size", brands.size());

        model.addAttribute("title", "Brand");
        model.addAttribute("brandNew", new Brand());
        return "brands";
    }

    @PostMapping("/add-brand")
    public String add(@ModelAttribute("brandNew") Brand brand, Model model, RedirectAttributes attributes) {

        if (!brandService.existsByName(brand.getName())) {
            brandService.save(brand);
            attributes.addFlashAttribute("success", "Brand added successfully!");
        }else {
            attributes.addFlashAttribute("failed", "Failed to add because duplicate name");
        }

        return "redirect:/brands";
    }

    @RequestMapping(value = "/findId", method = {RequestMethod.PUT, RequestMethod.GET})
    @ResponseBody
    public Optional<Brand> findById(Long id) {
        return brandService.findById(id);
    }

    @GetMapping("/update-brand")
    public String update(Brand brand, RedirectAttributes attributes) {
        try {
            brandService.update(brand);
            attributes.addFlashAttribute("success", "Updated Successfully!");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to update because duplicate name");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Error server");
        }

        return "redirect:/brands";
    }

    @RequestMapping(value = "/delete-brand", method = {RequestMethod.PUT, RequestMethod.GET})
    public String delete(Long id, RedirectAttributes attributes) {
        try {
            brandService.deletedById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to deleted");
        }

        return "redirect:/brands";
    }


    @GetMapping("/deleteID-brand/{id}")
    public String deleteItem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        brandService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Deleted successfully!");
        return "redirect:/brands";
    }

    @RequestMapping(value = "/enable-brand", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id, RedirectAttributes attributes) {

        try {
            brandService.enabledById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to enable!");
        }
        return "redirect:/brands";

    }





}
