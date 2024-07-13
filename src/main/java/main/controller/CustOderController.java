/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import jakarta.validation.Valid;
import java.util.List;
import main.dto.CustOrderDTO;
import main.handler.RessourceNotFoundException;
import main.service.CustOrderService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hp
 */
@RestController
@RequestMapping("/api/custorders")
@Validated
public class CustOderController {
    @Autowired
    public CustOderController(CustOrderService service) {
        this.service = service;
    }
    private final CustOrderService service;
    
    @GetMapping
    public ResponseEntity<List<CustOrderDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustOrderDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.findById(id));
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<CustOrderDTO> findByCode(@PathVariable String code){
        var c = service.findByCode(code);
        if(code.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @PostMapping
    public ResponseEntity<CustOrderDTO> create(@Valid @RequestBody CustOrderDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustOrderDTO> update(@PathVariable Integer id, @Valid @RequestBody CustOrderDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @PutMapping("/{orderid}/details/{detailid}")
    public ResponseEntity<Void> addDetail(@PathVariable Integer orderid, @PathVariable Integer detailid){
        service.adddetailToOrder(orderid, detailid);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{orderid}/details/{detailid}")
    public ResponseEntity<Void> removeDetail(@PathVariable Integer orderid, @PathVariable Integer detailid){
        service.removeDetailFromOrder(orderid, detailid);
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
