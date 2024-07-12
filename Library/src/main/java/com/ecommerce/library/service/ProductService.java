package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    /* Admin */
    List<ProductDto> findAll();
    Product save(MultipartFile imageProduct, ProductDto productDto);
    Product update(MultipartFile imageProduct,ProductDto productDto);
    void deleteById(Long id);
    void enableById(Long id);
    ProductDto getById(Long id);

    Page<ProductDto> pageProduct(int pageNo);

    Page<ProductDto> searchProducts(int pageNo,String keyword);


    /* Customer */
    List<Product> getAllProducts();
    List<Product> listViewProducts();

    Product getProductById(Long id);

    List<Product> getRelatedProducts(Long categoryId);

    List<ProductDto> findAllByCategory(String category);

    List<ProductDto> findByCategoryId(Long id);
    List<ProductDto> findByBrandId(Long id);

    List<Product> filterHighPrice();
    List<Product> filterLowPrice();

    List<Product> findByPriceRange(Double minPrice, Double maxPrice);

    List<Product> findByCategoryName(String categoryName);

    List<Product> findTop5ByOrderByIdDescWithLimit();

    List<ProductDto>  countAllProducts();


}
