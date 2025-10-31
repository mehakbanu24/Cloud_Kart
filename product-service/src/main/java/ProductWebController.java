

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductWebController {

  private final ProductService service;

  public ProductWebController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public String list(@RequestParam(value = "q", required = false) String q, Model model) {
    if (q != null && !q.isBlank()) model.addAttribute("products", service.searchByName(q));
    else model.addAttribute("products", service.findAll());
    model.addAttribute("q", q);
    return "products/list";
  }

  @GetMapping("/new")
  public String newForm(Model model) {
    model.addAttribute("product", new Product());
    return "products/new";
  }

  @PostMapping("/save")
  public String save(Product product) {
    service.save(product);
    return "redirect:/products";
  }

  @GetMapping("/{id}")
  public String details(@PathVariable Long id, Model model) {
    service.findById(id).ifPresent(p -> model.addAttribute("product", p));
    return "products/details";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    service.deleteById(id);
    return "redirect:/products";
  }
}