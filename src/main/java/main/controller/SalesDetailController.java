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
    
    @Operation(summary="Get All Sales Details", description="Get a List of Sales Details")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Sales Details"),
        @ApiResponse(responseCode="204", description="Found an empty list of Sales Details")
    })
    @GetMapping
    public ResponseEntity<List<SalesDetailDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get sales detail by Id", description="Return a single sales detail")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The sales detail"),
        @ApiResponse(responseCode="404", description="sales detail not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
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
    
    @Operation(summary="Create a new sales detail")
    @ApiResponse(responseCode="201", description="sales detail created successfully")
    @PostMapping
    public ResponseEntity<SalesDetailDTO> create(@Valid @RequestBody SalesDetailDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a sales detail")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="sales detail Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="sales detail not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
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
    
    @Operation(summary="Delete a sales detail")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="sales detail Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="sales detail not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
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
