/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import jakarta.validation.Valid;
import java.util.List;
import main.dto.SalesDetailDTO;
import main.handler.RessourceNotFoundException;
import main.service.SalesDetailService;
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
@RequestMapping("/api/salesdetails")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class SalesDetailController{
    @Autowired
    public SalesDetailController(SalesDetailService service) {
        this.service = service;
    }
    private final SalesDetailService service;
    
    @GetMapping
    public ResponseEntity<List<SalesDetailDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @GetMapping("/{productid}/{salesid}")
    public ResponseEntity<SalesDetailDTO> findById(@PathVariable Integer productid,@PathVariable Integer salesid ){
        if(productid<0 || salesid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(productid, salesid);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    
    @PostMapping
    public ResponseEntity<SalesDetailDTO> create(@Valid @RequestBody SalesDetailDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @PutMapping("/{productid}/{salesid}")
    public ResponseEntity<SalesDetailDTO> update(@PathVariable Integer productid,@PathVariable Integer salesid , @Valid @RequestBody SalesDetailDTO x){
        if(productid<0 || salesid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(productid,salesid)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(productid,salesid, x));
    }
    
    
    @DeleteMapping("/{productid}/{salesid}")
    public ResponseEntity<Void> delete(@PathVariable Integer productid, @PathVariable Integer salesid){
        if(productid<0 || salesid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(productid,salesid)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.delete(productid,salesid);
        return ResponseEntity.noContent().build();
    }
}
