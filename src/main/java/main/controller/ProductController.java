/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import main.dto.ProductDTO;
import main.dto.StockMvmDTO;
import main.handler.RessourceNotFoundException;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hp
 */
@RestController
@RequestMapping("/api")
@Validated
public class ProductController {
    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }
    private final ProductService service;
    
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.findById(id));
    }
    
    @GetMapping("/products/{name}")
    public ResponseEntity<ProductDTO> findByName(@PathVariable String name){
        var c = service.findByName(name);
        if(name.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id, @Valid @RequestBody ProductDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    //GET /api/products/search?name=example&categoryId=1&minPrice=10.00&maxPrice=100.00
    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDTO>> search(
            @RequestParam String name,
            @RequestParam Integer categoryId,
            @RequestBody BigDecimal minPrice,
            @RequestBody BigDecimal maxPrice
    ){
        return ResponseEntity.ok(service.searchProducts(name, categoryId, minPrice, maxPrice));
    }
    
    @GetMapping("/products/stock/{id}")
    public ResponseEntity<List<StockMvmDTO>> getproductHistory(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.getProductStockHistory(id));
    }
}
