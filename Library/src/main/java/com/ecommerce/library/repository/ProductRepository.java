package com.ecommerce.library.repository;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    /*Admin*/
    @Query("select p from Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
    Page<Product> searchProducts(String keyword,Pageable pageable);

    @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
    List<Product> searchProductsList(String keyword);

    @Query(value = "SELECT p.id as id, p.name as name, p.costPrice as costPrice FROM Product p WHERE p.is_activated = true AND p.is_deleted = false")
    List<ProductDto> countAllProducts();
    /* Customer */
    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false ")
    List<Product> getAllProducts();

    @Query(value = "select * from products p where p.is_deleted = false and p.is_activated = true limit 4", nativeQuery = true)
    List<Product> listViewProducts();

    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getRelatedProducts(Long categoryId);  /* change to brand*/


    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.name = ?1 and p.is_activated = true and p.is_deleted = false")
    List<Product> findAllByCategory(String category);

    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByCategoryId(Long id);

    @Query(value = "select p from Product p inner join Brand b on b.id = ?1 and p.brand.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByBrandId(Long id);
    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false order by p.costPrice desc")
    List<Product> filterHighPrice();

    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false order by p.costPrice ")
    List<Product> filterLowPrice();

    @Query("SELECT p FROM Product p WHERE p.costPrice BETWEEN ?1 AND ?2")
    List<Product> findByPriceRange(Double minPrice, Double maxPrice);

    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName AND p.is_activated = true AND p.is_deleted = false")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT p FROM Product p where p.is_activated = true ORDER BY p.id DESC limit 3")
    List<Product> findTop5ByOrderByIdDescWithLimit();

}
