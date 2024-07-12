package com.ecommerce.library.service;

import com.ecommerce.library.dto.BrandDto;
import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Brand;
import com.ecommerce.library.model.Category;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    List<Brand> findAll();
    Brand save(Brand brand);
    Optional<Brand> findById(Long id);
    Brand update(Brand brand);

    void deleteById(Long id);
    void deletedById(Long id);
    void enabledById(Long id);

    boolean existsByName(String name);
    List<Brand> findAllByActivated();

    /* Customer */
    List<BrandDto> getBrandsAndSize();

}
