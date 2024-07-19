package com.example.crud.product;

import com.example.crud.brand.Brand;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private float price;
    private LocalDate date;

    @Transient
    private int antiquity;


    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;



    public Product() {
    }

    public Product(Long id, String name, float price, LocalDate date, Brand brand) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.brand = brand;
    }

    public Product(String name, float price, LocalDate date, Brand brand) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAntiquity() {
        return Period.between(this.date, LocalDate.now()).getYears();
    }

    public void setAntiquity(int antiquity) {
        this.antiquity = antiquity;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}
