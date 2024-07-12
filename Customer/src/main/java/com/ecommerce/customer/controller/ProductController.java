package com.ecommerce.customer.controller;


import com.ecommerce.library.dto.BrandDto;
import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Brand;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.BrandService;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/shop")
    public String products(Model model){
        List<CategoryDto> categoryDtosList = categoryService.getCategoriesAndSize();
        List<BrandDto> brandDtoList = brandService.getBrandsAndSize();
        List<Product> products = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("categories",categoryDtosList);
        model.addAttribute("brands",brandDtoList);
        model.addAttribute("products",products);
        model.addAttribute("page", "Products");
        model.addAttribute("title", "Products");
        model.addAttribute("viewProducts",listViewProducts);
        return "shop";
    }

    /*актуален за product-detail*/
    @GetMapping("/find-product/{id}")
    public String findProductById(@PathVariable("id") Long id,Model model){
        Product product = productService.getProductById(id);
        Long categoryId = product.getCategory().getId(); /*change to brand*/
        Long brandId = product.getBrand().getId();

        List<Product> products = productService.getRelatedProducts(categoryId);

        model.addAttribute("product",product);
        model.addAttribute("products",products);
        model.addAttribute("page", "Product Detail");

        return "product-detail";
    }

    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.getById(id);
        List<ProductDto> productDtoList = productService.findAllByCategory(product.getCategory().getName());
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Product Detail");
        model.addAttribute("page", "Product Detail");
        model.addAttribute("productDetail", product);
        return "product-detail";
    }

    /* актуален */

    @GetMapping("/find-products/{id}")
    public String productsInCategory(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<BrandDto> brandDtos = brandService.getBrandsAndSize();

        // Филтриране на продуктите по категория и бранд
        List<ProductDto> productCategory = productService.findByCategoryId(id);
        List<ProductDto> productBrand = productService.findByBrandId(id);

        model.addAttribute("categories", categoryDtos);
        model.addAttribute("brands", brandDtos);

        List<Product> listView = productService.listViewProducts();

        model.addAttribute("productViews", listView);

        if (!productCategory.isEmpty()) {
            model.addAttribute("products", productCategory);
            model.addAttribute("title", productCategory.get(0).getCategory().getName());
            model.addAttribute("page", "Product-Category");
            return "shop";
        }

        if (!productBrand.isEmpty()) {
            model.addAttribute("products", productBrand);
            model.addAttribute("title", productBrand.get(0).getBrand().getName());
            model.addAttribute("page", "Product-Brand");
            return "shop";
        }

        // Добавете празен списък с продукти
            model.addAttribute("products", new ArrayList<>());
            model.addAttribute("error", "No products available for the selected category or brand.");


        return "shop";
    }


    @GetMapping("/find-products-by-brand/{brandId}")
    public String productsByBrand(@PathVariable("brandId") Long brandId, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<BrandDto> brandDtos = brandService.getBrandsAndSize();

        List<ProductDto> productBrand = productService.findByBrandId(brandId);

        model.addAttribute("categories", categoryDtos);
        model.addAttribute("brands", brandDtos);
        model.addAttribute("productViews", productService.listViewProducts());

        if (!productBrand.isEmpty()) {
            model.addAttribute("products", productBrand);
            model.addAttribute("title", productBrand.get(0).getBrand().getName());
            model.addAttribute("page", "Product-Brand");
        } else {
            model.addAttribute("products", new ArrayList<>());
            model.addAttribute("error", "No products available for the selected brand.");
        }

        return "shop";
    }

    @GetMapping("/clear-filters")
    public String clearFilters(Model model) {
        List<Product> allProducts = productService.getAllProducts();

        model.addAttribute("products", allProducts);
        return "shop";
    }

    @GetMapping("/find-productsB/{id}")
    public String productsInBrand(@PathVariable("id") Long id, Model model) {
        List<BrandDto> brandDtos = brandService.getBrandsAndSize();

        List<ProductDto> productDtos = productService.findByBrandId(id);
        List<Product> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("brands",brandDtos);
        model.addAttribute("title", productDtos.get(0).getBrand().getName());
        model.addAttribute("page", "Shop");
        model.addAttribute("products", productDtos);
        return "shop";
    }

    @GetMapping("/find-productss/{id}")
    public String productsInCategory1(@PathVariable("id") Long id, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();

        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<Product> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Shop");
        model.addAttribute("products", productDtos);
        return "home";
    }

    @GetMapping("/high-price")
    public String filterHighPrice(Model model){
        List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtoList = categoryService.getCategoriesAndSize();
        List<Brand> brands = brandService.findAllByActivated();
        List<BrandDto> brandDtoList = brandService.getBrandsAndSize();
        List<Product> products = productService.filterHighPrice();
        model.addAttribute("products",products);
        model.addAttribute("categories",categories);
        model.addAttribute("categoryDtoList",categoryDtoList);
        model.addAttribute("brands",brands);
        model.addAttribute("brandDtoList",brandDtoList);
        return "filter-high-price";
    }

    @GetMapping("/low-price")
    public String filterLowPrice(Model model){
        List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtoList = categoryService.getCategoriesAndSize();
        List<Brand> brands = brandService.findAllByActivated();
        List<BrandDto> brandDtoList = brandService.getBrandsAndSize();
        List<Product> products = productService.filterLowPrice();
        model.addAttribute("products",products);
        model.addAttribute("categories",categories);
        model.addAttribute("categoryDtoList",categoryDtoList);
        model.addAttribute("brands",brands);
        model.addAttribute("brandDtoList",brandDtoList);
        return "filter-low-price";
    }

    @GetMapping("/price-range")
    public String filterPriceRange(@RequestParam("min") Double minPrice, @RequestParam("max") Double maxPrice, Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        List<CategoryDto> categoryDtoList = categoryService.getCategoriesAndSize();

        List<Brand> brands = brandService.findAllByActivated();

        List<BrandDto> brandDtoList = brandService.getBrandsAndSize();

        List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryDtoList", categoryDtoList);
        model.addAttribute("brands",brands);
        model.addAttribute("brandDtoList",brandDtoList);
        return "price-range";
    }


    @GetMapping("/mens-watches")
    public String mensWatches(Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<Product> mensWatches = productService.findByCategoryName("Men");
        List<BrandDto> brandDtoList = brandService.getBrandsAndSize();
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("brands",brandDtoList);
        model.addAttribute("products", mensWatches);
        model.addAttribute("title", "Men's Watches");
        return "mens-watches";
    }


    @GetMapping("/womens-watches")
    public String womensWatches(Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<BrandDto> brandDtoList = brandService.getBrandsAndSize();

        List<Product> womensWatches = productService.findByCategoryName("Women");
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("brands",brandDtoList);
        model.addAttribute("products", womensWatches);
        model.addAttribute("title", "Women's Watches");
        return "womens-watches";
    }


}
