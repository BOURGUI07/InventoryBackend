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
import main.dto.SupplierDTO;
import main.handler.RessourceNotFoundException;
import main.service.SupplierService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hp
 */
@RestController
@RequestMapping("/api/suppliers")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class SupplierController {
    @Autowired
    public SupplierController(SupplierService service) {
        this.service = service;
    }
    private final SupplierService service;
    
    @Operation(summary="Get All suppliers", description="Get a List of suppliers")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of suppliers"),
        @ApiResponse(responseCode="204", description="Found an empty list of suppliers")
    })
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get supplier by Id", description="Return a single supplier")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The supplier"),
        @ApiResponse(responseCode="404", description="supplier not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get supplier by Name", description="Return a single supplier")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The supplier"),
        @ApiResponse(responseCode="404", description="supplier not found"),
        @ApiResponse(responseCode="400", description="The input name is blank")
    })
    @GetMapping("/search")
    public ResponseEntity<SupplierDTO> findByName(
            @RequestParam(required=false) String firstname,
            @RequestParam(required=false) String lastname){
        var c = service.findByFirstOrLastName(firstname,lastname);
        return ResponseEntity.ok(c);
    }
    
    @Operation(summary="Create a new supplier")
    @ApiResponse(responseCode="201", description="supplier created successfully")
    @PostMapping
    public ResponseEntity<SupplierDTO> create(@Valid @RequestBody SupplierDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a supplier")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="supplier Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="supplier not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> update(@PathVariable Integer id, @Valid @RequestBody SupplierDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Add order to a supplier")
    @PutMapping("/{supplierid}/orders/{orderid}")
    public ResponseEntity<Void> addOrder(@PathVariable Integer supplierid, @PathVariable Integer orderid){
        service.addOrder(supplierid, orderid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="remove order from a supplier")
    @DeleteMapping("/{supplierid}/orders/{orderid}")
    public ResponseEntity<Void> removeOrder(@PathVariable Integer supplierid, @PathVariable Integer orderid){
        service.removeOrder(supplierid, orderid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Delete a supplier")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="supplier Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="supplier not found"),
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
