package com.example.productservice.service;

import com.example.productservice.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
  Product save(Product product);
  Optional<Product> findById(Long id);
  List<Product> findAll();
  void deleteById(Long id);
  List<Product> searchByName(String query);
}
