package com.example.crud.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<Brand> getBrands() {
        return brandService.getBrands();
    }

    @PostMapping
    public ResponseEntity<Object> brandRegister(@RequestBody Brand brand) {
        return brandService.newBrand(brand);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBrand(@PathVariable Long id, @RequestBody Brand brand) {
        return brandService.updateBrand(id, brand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> brandDelete(@PathVariable Long id) {
        return brandService.deleteBrand(id);
    }
}
