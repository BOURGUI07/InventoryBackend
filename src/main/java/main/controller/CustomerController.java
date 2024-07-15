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
import main.dto.CustomerDTO;
import main.handler.RessourceNotFoundException;
import main.service.CustomerService;
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
@RequestMapping("/api/customers")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class CustomerController {
    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }
    private final CustomerService service;
    
    @Operation(summary="Get All Customers", description="Get a List of Customers")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Customers"),
        @ApiResponse(responseCode="204", description="Found an empty list of Customers")
    })
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get customer by Id", description="Return a single customer")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The customer"),
        @ApiResponse(responseCode="404", description="customer not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable Integer id){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get customer by first or lastname", description="Return a single customer")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The customer"),
        @ApiResponse(responseCode="404", description="customer not found")
    })
    @GetMapping("/search")
    public ResponseEntity<CustomerDTO> findByName(
            @RequestParam(required=false) String firstname,
            @RequestParam(required=false) String lastname){
        var c = service.findByFirstOrLastName(firstname,lastname);
        return ResponseEntity.ok(c);
    }
    
    @Operation(summary="Create a new customer")
    @ApiResponse(responseCode="201", description="customer created successfully")
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a customer")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="customer Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="customer not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Integer id, @Valid @RequestBody CustomerDTO x){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Add order to a customer")
    @PutMapping("/{customerid}/orders/{orderid}")
    public ResponseEntity<Void> addOrder(@PathVariable Integer customerid, @PathVariable Integer orderid){
        service.addOrder(customerid, orderid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="remove order from a customer")
    @DeleteMapping("/{customerid}/orders/{orderid}")
    public ResponseEntity<Void> removeOrder(@PathVariable Integer customerid, @PathVariable Integer orderid){
        service.removeOrder(customerid, orderid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Delete a customer")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="customer Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="customer not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
