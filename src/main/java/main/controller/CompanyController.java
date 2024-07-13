/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import jakarta.validation.Valid;
import java.util.List;
import main.dto.CompanyDTO;
import main.handler.RessourceNotFoundException;
import main.service.CompanyService;
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
@RequestMapping("/api/companies")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class CompanyController {
    @Autowired
    public CompanyController(CompanyService service) {
        this.service = service;
    }
    private final CompanyService service;
    
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @GetMapping("/{name}")
    public ResponseEntity<CompanyDTO> findByName(@PathVariable String name){
        var c = service.findByName(name);
        if(name.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @PostMapping
    public ResponseEntity<CompanyDTO> create(@Valid @RequestBody CompanyDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> update(@PathVariable Integer id, @Valid @RequestBody CompanyDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @PutMapping("/{companyid}/products/{productid}")
    public ResponseEntity<Void> addProduct(@PathVariable Integer companyid, @PathVariable Integer productid){
        service.addProductToCompany(companyid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{companyid}/products/{productid}")
    public ResponseEntity<Void> removeProduct(@PathVariable Integer companyid, @PathVariable Integer productid){
        service.removeProductFromCompany(companyid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{companyid}/users/{userid}")
    public ResponseEntity<Void> addUser(@PathVariable Integer companyid, @PathVariable Integer userid){
        service.addUserToCompany(companyid, userid);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{companyid}/users/{userid}")
    public ResponseEntity<Void> removeUser(@PathVariable Integer companyid, @PathVariable Integer userid){
        service.removeUserFromCompany(companyid, userid);
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
