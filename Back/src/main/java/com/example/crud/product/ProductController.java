package com.example.crud.product;

import com.example.crud.brand.Brand;
import com.example.crud.brand.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;

    @Autowired
    public ProductController(ProductService productService, BrandService brandService) {
        this.productService = productService;
        this.brandService = brandService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @PostMapping
    public ResponseEntity<Object> productRegister(@RequestBody Product product) {
        // Asocia la marca al producto
        Brand brand = product.getBrand();
        if (brand == null || brand.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La marca no puede ser nula");
        }

        // Valida que la marca exista
        Brand existingBrand = brandService.findBrandById(brand.getId());
        if (existingBrand == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marca no encontrada");
        }
        product.setBrand(existingBrand);

        return productService.newProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Validar y asociar la marca al producto
        Brand brand = product.getBrand();
        if (brand == null || brand.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La marca no puede ser nula");
        }

        Brand existingBrand = brandService.findBrandById(brand.getId());
        if (existingBrand == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marca no encontrada");
        }
        product.setBrand(existingBrand);

        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> productDelete(@PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }
}
