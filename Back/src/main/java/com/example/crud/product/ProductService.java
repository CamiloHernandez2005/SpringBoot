package com.example.crud.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private HashMap<String, Object> datos;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return this.productRepository.findAll();
    }

    public ResponseEntity<Object> newProduct(Product product) {
        Optional<Product> res = productRepository.findProductByName(product.getName());
        datos = new HashMap<>();

        if (res.isPresent() && product.getId() == null) {
            datos.put("error", true);
            datos.put("message", "Ya existe un producto con ese nombre");
            return new ResponseEntity<>(datos, HttpStatus.CONFLICT);
        }

        productRepository.save(product);
        datos.put("message", "Se guardó correctamente");
        datos.put("data", product);

        return new ResponseEntity<>(datos, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updateProduct(Long id, Product product) {
        datos = new HashMap<>();

        if (!productRepository.existsById(product.getId())) {
            datos.put("error", true);
            datos.put("message", "No existe un producto con ese ID");
            return new ResponseEntity<>(datos, HttpStatus.NOT_FOUND);
        }

        // Actualiza los campos del producto
        Product updatedProduct = productRepository.save(product);

        datos.put("message", "Producto actualizado");
        datos.put("data", updatedProduct);

        return new ResponseEntity<>(datos, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteProduct(Long id) {
        datos = new HashMap<>();
        boolean exist = this.productRepository.existsById(id);

        if (!exist) {
            datos.put("error", true);
            datos.put("message", "No existe un producto con ese ID");
            return new ResponseEntity<>(datos, HttpStatus.CONFLICT);
        }

        productRepository.deleteById(id);
        datos.put("message", "Producto eliminado");

        return new ResponseEntity<>(datos, HttpStatus.ACCEPTED);
    }
}
