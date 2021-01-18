package com.luthfihariz.esbasic;

import com.luthfihariz.esbasic.dto.SearchQueryDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    
    @GetMapping()
    public List<Product> getProduct() {
        // TODO
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {

    }

    @DeleteMapping("/{id}")
    public void deleteProduct() {

    }

    @PutMapping("/{id}")
    public void updateProduct() {

    }

    @PostMapping("/search")
    public void searchProduct(SearchQueryDto searchQueryDto) {

    }

}
