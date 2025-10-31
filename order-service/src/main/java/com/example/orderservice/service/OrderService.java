package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.OrderEntity;
import java.util.List;

public interface OrderService {
  OrderEntity placeOrder(OrderRequest request);
  List<OrderEntity> listOrders();
  List<OrderEntity> listOrdersByUser(Long userId);
}
