/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import jakarta.validation.Valid;
import java.util.List;
import main.dto.SuppOrderDetailDTO;
import main.handler.RessourceNotFoundException;
import main.service.SuppOrderDetailService;
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
@RequestMapping("/api/suppdetails")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class SuppOrderDetailController {
    @Autowired
    public SuppOrderDetailController(SuppOrderDetailService service) {
        this.service = service;
    }
    private final SuppOrderDetailService service;
    
    @GetMapping
    public ResponseEntity<List<SuppOrderDetailDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @GetMapping("/{productid}/{orderid}")
    public ResponseEntity<SuppOrderDetailDTO> findById(@PathVariable Integer productid,@PathVariable Integer orderid ){
        if(productid<0 || orderid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(productid,orderid);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    
    @PostMapping
    public ResponseEntity<SuppOrderDetailDTO> create(@Valid @RequestBody SuppOrderDetailDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @PutMapping("/{productid}/{orderid}")
    public ResponseEntity<SuppOrderDetailDTO> update(@PathVariable Integer productid,@PathVariable Integer orderid , @Valid @RequestBody SuppOrderDetailDTO x){
        if(productid<0 || orderid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(productid,orderid)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(productid,orderid, x));
    }
    
    
    @DeleteMapping("/{productid}/{orderid}")
    public ResponseEntity<Void> delete(@PathVariable Integer productid, @PathVariable Integer orderid){
        if(productid<0 || orderid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(productid,orderid)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.delete(productid,orderid);
        return ResponseEntity.noContent().build();
    }
}
