package com.ecommerce.library.repository;

import com.ecommerce.library.dto.BrandDto;
import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Brand;
import com.ecommerce.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    boolean existsByName(String name);

    @Query("select b from Brand b where b.is_activated = true and b.is_deleted = false")
    List<Brand> findAllByActivated();


    @Query(value = "select new com.ecommerce.library.dto.BrandDto(b.id, b.name, count(p.brand.id)) " +
            "from Brand b left join Product p on b.id = p.brand.id " +
            "where b.is_activated = true and b.is_deleted = false " +
            "group by b.id ")
    List<BrandDto> getBrandsAndSize();
}
