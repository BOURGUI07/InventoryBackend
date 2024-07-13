/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import jakarta.validation.Valid;
import java.util.List;
import main.dto.CustomerDTO;
import main.handler.RessourceNotFoundException;
import main.service.CustomerService;
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
@RequestMapping("/api/customers")
@Validated
public class CustomerController {
    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }
    private final CustomerService service;
    
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.findById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<CustomerDTO> findByName(
            @RequestParam(required=false) String firstname,
            @RequestParam(required=false) String lastname){
        var c = service.findByFirstOrLastName(firstname,lastname);
        return ResponseEntity.ok(c);
    }
    
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Integer id, @Valid @RequestBody CustomerDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @PutMapping("/{customerid}/orders/{orderid}")
    public ResponseEntity<Void> addOrder(@PathVariable Integer customerid, @PathVariable Integer orderid){
        service.addOrder(customerid, orderid);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{customerid}/orders/{orderid}")
    public ResponseEntity<Void> removeOrder(@PathVariable Integer customerid, @PathVariable Integer orderid){
        service.removeOrder(customerid, orderid);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
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
