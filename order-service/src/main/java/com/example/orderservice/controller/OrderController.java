package com.example.orderservice.controller;



import com.example.orderservice.dto.*;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;
  private final RestTemplate restTemplate;

  @Value("${user.service.url}")
  private String userServiceUrl;

  @Value("${product.service.url}")
  private String productServiceUrl;

  public OrderController(OrderService orderService, RestTemplate restTemplate) {
    this.orderService = orderService;
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public String list(Model model) {
    List<OrderEntity> orders = orderService.listOrders();
    model.addAttribute("orders", orders);
    return "orders/list";
  }

  @GetMapping("/new")
  public String newForm(Model model) {
    UserDTO[] users = restTemplate.getForObject(userServiceUrl, UserDTO[].class);
    ProductDTO[] products = restTemplate.getForObject(productServiceUrl, ProductDTO[].class);

    model.addAttribute("users", users != null ? Arrays.asList(users) : List.of());
    model.addAttribute("products", products != null ? Arrays.asList(products) : List.of());
    model.addAttribute("orderRequest", new OrderRequest());
    return "orders/new";
  }

  @PostMapping
  public String place(@ModelAttribute OrderRequest orderRequest, Model model) {
    try {
      orderService.placeOrder(orderRequest);
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      // reload lists to redisplay form with error
      UserDTO[] users = restTemplate.getForObject(userServiceUrl, UserDTO[].class);
      ProductDTO[] products = restTemplate.getForObject(productServiceUrl, ProductDTO[].class);
      model.addAttribute("users", users != null ? Arrays.asList(users) : List.of());
      model.addAttribute("products", products != null ? Arrays.asList(products) : List.of());
      return "orders/new";
    }
    return "redirect:/orders";
  }
}