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
import main.dto.StockMvmDTO;
import main.handler.RessourceNotFoundException;
import main.service.StockMvmService;
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
@RequestMapping("/api/stockmvms")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class StockMvmController {
    @Autowired
    public StockMvmController(StockMvmService service) {
        this.service = service;
    }
    private final StockMvmService service;
    
    @Operation(summary="Get All Stock Movements", description="Get a List of Stock Movements")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Stock Movements"),
        @ApiResponse(responseCode="204", description="Found an empty list of Stock Movements")
    })
    @GetMapping
    public ResponseEntity<List<StockMvmDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get stock movement by Id", description="Return a single stock movement")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The stock movement"),
        @ApiResponse(responseCode="404", description="stock movement not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StockMvmDTO> findById(@PathVariable Integer id){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s= service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    
    @Operation(summary="Create a new stock movement")
    @ApiResponse(responseCode="201", description="stock movement created successfully")
    @PostMapping
    public ResponseEntity<StockMvmDTO> create(@Valid @RequestBody StockMvmDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a stock movement")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="stock movement Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="stock movement not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StockMvmDTO> update(@PathVariable Integer id, @Valid @RequestBody StockMvmDTO x){
        if(id<0 || id>=service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Delete a stock movement")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="stock movement Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="stock movement not found"),
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
