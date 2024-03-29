package org.fasttrackit.onlineshop.web;


import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.transfer.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody SaveProductRequest saveProductRequest){
        Product product = productService.createProduct(saveProductRequest);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody SaveProductRequest saveProductRequest,@PathVariable Long id){
        Product product = productService.updateProduct(saveProductRequest,id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        Product product = productService.getProduct(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(GetProductsRequest getProductsRequest, Pageable pageable){
        Page<Product> products = productService.getProducts(getProductsRequest, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
