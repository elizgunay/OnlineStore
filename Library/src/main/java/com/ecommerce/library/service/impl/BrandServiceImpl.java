package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.BrandDto;
import com.ecommerce.library.model.Brand;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.BrandRepository;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand save(Brand brand) {
        try {
            Brand brandSave = new Brand(brand.getName());
            return brandRepository.save(brandSave);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Brand update(Brand brand) {
        Brand brandUpdate = brandRepository.getReferenceById(brand.getId());
        brandUpdate.setName(brand.getName());
        return brandRepository.save(brandUpdate);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public void deletedById(Long id) {
        Brand brand = brandRepository.getReferenceById(id);

        brand.set_deleted(true);
        brand.set_activated(false);
        brandRepository.save(brand);
    }



    @Override
    public void enabledById(Long id) {
        Brand brand = brandRepository.getById(id);
        brand.set_activated(true);
        brand.set_deleted(false);
        brandRepository.save(brand);
    }

    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public List<Brand> findAllByActivated() {
        return brandRepository.findAllByActivated();

    }

    @Override
    public List<BrandDto> getBrandsAndSize() {
        return  brandRepository.getBrandsAndSize();
    }


}
