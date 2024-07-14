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
import main.dto.CustOrderDetailDTO;
import main.handler.RessourceNotFoundException;
import main.service.CustOrderDetailService;
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
@RequestMapping("/api/custdetails")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class CustOrderDetailController {
    @Autowired
    public CustOrderDetailController(CustOrderDetailService service) {
        this.service = service;
    }
    private final CustOrderDetailService service;
    
    @Operation(summary="Get All Customer Order Details", description="Get a List of Customer Order Details")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Customer Order Details"),
        @ApiResponse(responseCode="204", description="Found an empty list of Customer Order Details")
    })
    @GetMapping
    public ResponseEntity<List<CustOrderDetailDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get customer order detail by Id", description="Return a single customer order detail")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The customer order detail"),
        @ApiResponse(responseCode="404", description="customer order detail not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/{productid}/{orderid}")
    public ResponseEntity<CustOrderDetailDTO> findById(@PathVariable Integer productid,@PathVariable Integer orderid ){
        if(productid<0 || orderid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(productid, orderid);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    
    @Operation(summary="Create a new customer order detail")
    @ApiResponse(responseCode="201", description="customer order detail created successfully")
    @PostMapping
    public ResponseEntity<CustOrderDetailDTO> create(@Valid @RequestBody CustOrderDetailDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a customer order detail")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="customer order detail Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="customer order detail not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/{productid}/{orderid}")
    public ResponseEntity<CustOrderDetailDTO> update(@PathVariable Integer productid,@PathVariable Integer orderid , @Valid @RequestBody CustOrderDetailDTO x){
        if(productid<0 || orderid<0){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(productid,orderid)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(productid,orderid, x));
    }
    
    @Operation(summary="Delete a customer order detail")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="customer order detail Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="customer order detail not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
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
