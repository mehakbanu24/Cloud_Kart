package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserWebController {
  private final UserService userService;

  public UserWebController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("users", userService.findAll());
    return "users/list";
  }

  @GetMapping("/new")
  public String newForm(Model model) {
    model.addAttribute("user", new User());
    return "users/new";
  }

  @PostMapping("/save")
  public String save(User user) {
    userService.save(user);
    return "redirect:/users";
  }

  @GetMapping("/{id}")
  public String details(@PathVariable Long id, Model model) {
    userService.findById(id).ifPresent(u -> model.addAttribute("user", u));
    return "users/details";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    userService.deleteById(id);
    return "redirect:/users";
  }
}
