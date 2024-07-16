/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import main.dto.CategoryDTO;
import main.dto.CompanyDTO;
import main.handler.RessourceNotFoundException;
import main.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/api/companies")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class CompanyController {
    @Autowired
    public CompanyController(CompanyService service) {
        this.service = service;
    }
    private final CompanyService service;
    
    @Operation(summary="Get All Companies", description="Get a List of Companies")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Companies"),
        @ApiResponse(responseCode="204", description="Found an empty list of Companies")
    })
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get company by Id", description="Return a single company")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The company"),
        @ApiResponse(responseCode="404", description="company not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> findById(@PathVariable Integer id){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get company by Name", description="Return a single company")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The company"),
        @ApiResponse(responseCode="404", description="company not found"),
        @ApiResponse(responseCode="400", description="The input name is blank")
    })
    @GetMapping("/name/{name}")
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
    
    @Operation(summary="Create a new company")
    @ApiResponse(responseCode="201", description="company created successfully")
    @PostMapping
    public ResponseEntity<CompanyDTO> create(@Valid @RequestBody CompanyDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a company")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="company Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="company not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> update(@PathVariable Integer id, @Valid @RequestBody CompanyDTO x){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Add product to a company")
    @PutMapping("/{companyid}/products/{productid}")
    public ResponseEntity<Void> addProduct(@PathVariable Integer companyid, @PathVariable Integer productid){
        service.addProductToCompany(companyid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Remove product from a company")
    @DeleteMapping("/{companyid}/products/{productid}")
    public ResponseEntity<Void> removeProduct(@PathVariable Integer companyid, @PathVariable Integer productid){
        service.removeProductFromCompany(companyid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Add user to a company")
    @PutMapping("/{companyid}/users/{userid}")
    public ResponseEntity<Void> addUser(@PathVariable Integer companyid, @PathVariable Integer userid){
        service.addUserToCompany(companyid, userid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Remove user from a company")
    @DeleteMapping("/{companyid}/users/{userid}")
    public ResponseEntity<Void> removeUser(@PathVariable Integer companyid, @PathVariable Integer userid){
        service.removeUserFromCompany(companyid, userid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Delete a company")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="company Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="company not found"),
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
    
    @Operation(summary = "Get paginated list of companies", description = "Retrieve a paginated list of companies with optional search and sorting capabilities.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found paginated list of companies"),
        @ApiResponse(responseCode = "204", description = "No companies found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping
    public ResponseEntity<Page<CompanyDTO>> findAllPaginated(
            @RequestParam (defaultValue="0") int page,
            @RequestParam (defaultValue="10") int size,
            @RequestParam (defaultValue="id,asc") String[] sort,
            @RequestParam (required = false) String name
    ){
        var orders = new ArrayList<Sort.Order>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(Sort.Direction.fromString(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(Sort.Direction.fromString(sort[1]), sort[0]));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<CompanyDTO> pageCompanies = service.findAllPaginated(pageable, name);
        
        if (pageCompanies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        
        return ResponseEntity.ok(pageCompanies);
    }
}
