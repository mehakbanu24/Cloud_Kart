package com.example.orderservice.entity;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  private Long productId;
  private String productName;
  private Integer quantity;
  private Double totalPrice;
  private String status;
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() { createdAt = LocalDateTime.now(); }
}
