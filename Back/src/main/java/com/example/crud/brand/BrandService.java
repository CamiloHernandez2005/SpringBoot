package com.example.crud.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    private HashMap<String, Object> datos;
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getBrands() {
        return this.brandRepository.findAll();
    }

    public ResponseEntity<Object> newBrand(Brand brand) {
        Optional<Brand> res = brandRepository.findBrandByName(brand.getName());
        datos = new HashMap<>();

        if (res.isPresent() && brand.getId() == null) {
            datos.put("error", true);
            datos.put("message", "Ya existe una marca con ese nombre");
            return new ResponseEntity<>(datos, HttpStatus.CONFLICT);
        }

        brandRepository.save(brand);
        datos.put("message", "Se guardó correctamente");
        datos.put("data", brand);

        return new ResponseEntity<>(datos, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updateBrand(Long id, Brand brand) {
        datos = new HashMap<>();

        if (!brandRepository.existsById(brand.getId())) {
            datos.put("error", true);
            datos.put("message", "No existe una marca con ese ID");
            return new ResponseEntity<>(datos, HttpStatus.NOT_FOUND);
        }

        // Actualiza los campos de la marca
        Brand updatedBrand = brandRepository.save(brand);

        datos.put("message", "Marca actualizada");
        datos.put("data", updatedBrand);

        return new ResponseEntity<>(datos, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteBrand(Long id) {
        datos = new HashMap<>();
        boolean exist = this.brandRepository.existsById(id);

        if (!exist) {
            datos.put("error", true);
            datos.put("message", "No existe una marca con ese ID");
            return new ResponseEntity<>(datos, HttpStatus.CONFLICT);
        }

        brandRepository.deleteById(id);
        datos.put("message", "Marca eliminada");

        return new ResponseEntity<>(datos, HttpStatus.ACCEPTED);
    }
}
