package com.example.orderservice.dto;


import lombok.Data;
@Data
public class OrderRequest { private Long userId; private Long productId; private Integer quantity = 1; }
