package com.example.crud.product;

import com.example.crud.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);
    @Query("SELECT p FROM Product p JOIN FETCH p.brand")
    List<Product> findAllWithBrand();
}