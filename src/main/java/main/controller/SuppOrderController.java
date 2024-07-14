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
import main.dto.SuppOrderDTO;
import main.handler.RessourceNotFoundException;
import main.service.SuppOrderService;
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
@RequestMapping("/api/supporders")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class SuppOrderController {
    @Autowired
    public SuppOrderController(SuppOrderService service) {
        this.service = service;
    }
    private final SuppOrderService service;
    
    @Operation(summary="Get All Supplier Orders", description="Get a List of Supplier Orders")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Supplier Orders"),
        @ApiResponse(responseCode="204", description="Found an empty list of Supplier Orders")
    })
    @GetMapping
    public ResponseEntity<List<SuppOrderDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get Supplier order by Id", description="Return a single Supplier order")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The Supplier order"),
        @ApiResponse(responseCode="404", description="Supplier order not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuppOrderDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s= service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get Supplier order by code", description="Return a single Supplier order")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The Supplier order"),
        @ApiResponse(responseCode="404", description="Supplier order not found"),
        @ApiResponse(responseCode="400", description="The input code is blank")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<SuppOrderDTO> findByCode(@PathVariable String code){
        var c = service.findByCode(code);
        if(code.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @Operation(summary="Create a new supplier order")
    @ApiResponse(responseCode="201", description="supplier order created successfully")
    @PostMapping
    public ResponseEntity<SuppOrderDTO> create(@Valid @RequestBody SuppOrderDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a supplier order")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="supplier order Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="supplier order not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuppOrderDTO> update(@PathVariable Integer id, @Valid @RequestBody SuppOrderDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Add order detail to a supplier order")
    @PutMapping("/{orderid}/details/{detailid}")
    public ResponseEntity<Void> addDetail(@PathVariable Integer orderid, @PathVariable Integer detailid){
        service.adddetailToOrder(orderid, detailid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="remove order detail from a supplier order")
    @DeleteMapping("/{orderid}/details/{detailid}")
    public ResponseEntity<Void> removeDetail(@PathVariable Integer orderid, @PathVariable Integer detailid){
        service.removeDetailFromOrder(orderid, detailid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Delete a supplier order")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="supplier order Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="supplier order not found"),
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
