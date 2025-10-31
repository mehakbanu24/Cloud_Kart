package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository repo;

  public ProductServiceImpl(ProductRepository repo) {
    this.repo = repo;
  }

  @Override
  public Product save(Product product) {
    return repo.save(product);
  }

  @Override
  public Optional<Product> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public List<Product> findAll() {
    return repo.findAll();
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }

  @Override
  public List<Product> searchByName(String query) {
    return repo.findByNameContainingIgnoreCase(query);
  }
}
