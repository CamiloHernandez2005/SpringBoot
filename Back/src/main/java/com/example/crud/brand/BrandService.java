package com.example.crud.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getBrands() {
        return brandRepository.findAll();
    }

    public ResponseEntity<Object> newBrand(Brand brand) {
        Optional<Brand> brandOptional = brandRepository.findBrandByName(brand.getName());
        if (brandOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Brand already exists");
        }
        brandRepository.save(brand);
        return ResponseEntity.ok().body("Brand created successfully");
    }

    public ResponseEntity<Object> updateBrand(Long id, Brand brandDetails) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Brand not found with id " + id));

        brand.setName(brandDetails.getName());
        brandRepository.save(brand);
        return ResponseEntity.ok().body("Brand updated successfully");
    }

    public ResponseEntity<Object> deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Brand not found with id " + id));

        brandRepository.delete(brand);
        return ResponseEntity.ok().body("Brand deleted successfully");
    }

    public Brand findBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }
}
