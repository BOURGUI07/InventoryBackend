/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import main.dto.SalesDTO;
import main.handler.RessourceNotFoundException;
import main.service.SalesService;
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
@RequestMapping("/api/sales")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class SalesController {
    @Autowired
    public SalesController(SalesService service) {
        this.service = service;
    }
    private final SalesService service;
    
    @Operation(summary="Get All Sales", description="Get a List of Sales")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Sales"),
        @ApiResponse(responseCode="204", description="Found an empty list of Sales")
    })
    @GetMapping
    public ResponseEntity<List<SalesDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get sale by Id", description="Return a single sale")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The sale"),
        @ApiResponse(responseCode="404", description="sale not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SalesDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get sale by code", description="Return a single sale")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The sale"),
        @ApiResponse(responseCode="404", description="sale not found"),
        @ApiResponse(responseCode="400", description="The input code is blank")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<SalesDTO> findByCode(@PathVariable String code){
        var c = service.findByCode(code);
        if(code.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @Operation(summary="Create a new sale")
    @ApiResponse(responseCode="201", description="sale created successfully")
    @PostMapping
    public ResponseEntity<SalesDTO> create(@Valid @RequestBody SalesDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a sale")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="sale Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="sale not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SalesDTO> update(@PathVariable Integer id, @Valid @RequestBody SalesDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Add sales detail to a sale")
    @PutMapping("/{salesid}/details/{detailid}")
    public ResponseEntity<Void> addDetail(@PathVariable Integer salesid, @PathVariable Integer detailid){
        service.addDetail(salesid, detailid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="remove a sales detail from a sale")
    @DeleteMapping("/{salesid}/details/{detailid}")
    public ResponseEntity<Void> removeDetail(@PathVariable Integer salesid, @PathVariable Integer detailid){
        service.removeDetail(salesid, detailid);
        return ResponseEntity.noContent().build();
    }
    
    
    @Operation(summary="Delete a sale")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="sale Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="sale not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
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
