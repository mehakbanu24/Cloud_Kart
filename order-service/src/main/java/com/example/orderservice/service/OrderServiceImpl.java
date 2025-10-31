package com.example.orderservice.service;

import com.example.orderservice.dto.*;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository repo;
  private final RestTemplate restTemplate;

  @Value("${user.service.url}")
  private String userServiceUrl;

  @Value("${product.service.url}")
  private String productServiceUrl;

  public OrderServiceImpl(OrderRepository repo, RestTemplate restTemplate) {
    this.repo = repo;
    this.restTemplate = restTemplate;
  }

  @Override
  public OrderEntity placeOrder(OrderRequest request) {
    // 1) Validate user exists
    UserDTO user;
    try {
      user = restTemplate.getForObject(userServiceUrl + "/" + request.getUserId(), UserDTO.class);
    } catch (RestClientException ex) {
      throw new RuntimeException("User service unreachable or user not found");
    }
    if (user == null) throw new RuntimeException("User not found");

    // 2) Validate product exists & stock
    ProductDTO product;
    try {
      product = restTemplate.getForObject(productServiceUrl + "/" + request.getProductId(), ProductDTO.class);
    } catch (RestClientException ex) {
      throw new RuntimeException("Product service unreachable or product not found");
    }
    if (product == null) throw new RuntimeException("Product not found");
    if (product.getStock() == null || product.getStock() < request.getQuantity())
      throw new RuntimeException("Insufficient stock");

    // 3) Reduce stock on product-service
    try {
      restTemplate.put(productServiceUrl + "/reduceStock/" + request.getProductId() + "/" + request.getQuantity(), null);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update product stock: " + e.getMessage());
    }

    // 4) Save order
    OrderEntity order = new OrderEntity();
    order.setUserId(request.getUserId());
    order.setProductId(request.getProductId());
    order.setProductName(product.getName());
    order.setQuantity(request.getQuantity());
    order.setTotalPrice(product.getPrice().doubleValue() * request.getQuantity());
    order.setStatus("PLACED");
    return repo.save(order);
  }

  @Override
  public List<OrderEntity> listOrders() { return repo.findAll(); }

  @Override
  public List<OrderEntity> listOrdersByUser(Long userId) { return repo.findByUserId(userId); }
}