package com.example.crud.product;

import com.example.crud.brand.Brand;
import com.example.crud.brand.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAllWithBrand();
    }

    public ResponseEntity<Object> newProduct(Product product) {
        // Validar y asociar la marca al producto
        Brand brand = brandRepository.findById(product.getBrand().getId())
                .orElseThrow(() -> new IllegalStateException("Brand not found with id " + product.getBrand().getId()));
        product.setBrand(brand);

        productRepository.save(product);
        return ResponseEntity.ok().body("Product created successfully");
    }

    public ResponseEntity<Object> updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found with id " + id));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setDate(productDetails.getDate());

        // Validar y asociar la marca al producto
        Brand brand = brandRepository.findById(productDetails.getBrand().getId())
                .orElseThrow(() -> new IllegalStateException("Brand not found with id " + productDetails.getBrand().getId()));
        product.setBrand(brand);

        productRepository.save(product);
        return ResponseEntity.ok().body("Product updated successfully");
    }

    public ResponseEntity<Object> deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product not found with id " + id));

        productRepository.delete(product);
        return ResponseEntity.ok().body("Product deleted successfully");
    }
}
