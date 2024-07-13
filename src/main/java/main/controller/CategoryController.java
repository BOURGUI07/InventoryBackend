/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import jakarta.validation.Valid;
import java.util.List;
import main.dto.CategoryDTO;
import main.handler.RessourceNotFoundException;
import main.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hp
 */
@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class CategoryController {
    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }
    private final CategoryService service;
    
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> findAll(){
        var s= service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @GetMapping("/categories/{name}")
    public ResponseEntity<CategoryDTO> findByName(@PathVariable String name){
        var c = service.findByName(name);
        if(name.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Integer id, @Valid @RequestBody CategoryDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @PutMapping("/categories/{categoryid}/products/{productid}")
    public ResponseEntity<Void> addProduct(@PathVariable Integer categoryid, @PathVariable Integer productid){
        service.addProductToCateg(categoryid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/categories/{categoryid}/products/{productid}")
    public ResponseEntity<Void> removeProduct(@PathVariable Integer categoryid, @PathVariable Integer productid){
        service.addProductToCateg(categoryid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/categories/{id}")
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
}
