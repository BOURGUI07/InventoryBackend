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
import main.handler.RessourceNotFoundException;
import main.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class CategoryController {
    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }
    private final CategoryService service;
    
    @Operation(summary="Get All Categories", description="Get a List of Categories")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of Categories"),
        @ApiResponse(responseCode="204", description="Found an empty list of categories")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> findAll(){
        var s= service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get Category by Id", description="Return a single category")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The category"),
        @ApiResponse(responseCode="404", description="Category not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Integer id){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get Category By Name", description="Return a single category")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully the category"),
        @ApiResponse(responseCode="404", description="category not found"),
        @ApiResponse(responseCode="400", description="The input name is blank")
    })
    @GetMapping("/categories/name/{name}")
    public ResponseEntity<CategoryDTO> findByName(@PathVariable String name){
        var c = service.findByName(name);
        if(name.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @Operation(summary="Create a new category")
    @ApiResponse(responseCode="201", description="Category created successfully")
    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a Category")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Category Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="category not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Integer id, @Valid @RequestBody CategoryDTO x){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Add product to a category")
    @ApiResponse(responseCode="204", description="Product added sucessfully to the category")
    @PutMapping("/categories/{categoryid}/products/{productid}")
    public ResponseEntity<Void> addProduct(@PathVariable Integer categoryid, @PathVariable Integer productid){
        service.addProductToCateg(categoryid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Remove product to a category")
    @ApiResponse(responseCode="204", description="Product Removed sucessfully from the category")
    @DeleteMapping("/categories/{categoryid}/products/{productid}")
    public ResponseEntity<Void> removeProduct(@PathVariable Integer categoryid, @PathVariable Integer productid){
        service.addProductToCateg(categoryid, productid);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary="Delete a Category")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="Category Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="category not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @DeleteMapping("/categories/{id}")
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
    
    /*
    Pagination: GET /api/categories?page=1&size=5
    Sorting: GET /api/categories?sort=name,asc
    Filtering: GET /api/categories?name=electronics
    Combined: GET /api/categories?page=1&size=5&sort=name,asc&name=electronics&desc=devices
    */
    @Operation(summary = "Get paginated list of categories", description = "Retrieve a paginated list of categories with optional search and sorting capabilities.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found paginated list of categories"),
        @ApiResponse(responseCode = "204", description = "No categories found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/categories")
    public ResponseEntity<Page<CategoryDTO>> findAllPaginated(
            @RequestParam (defaultValue="0") int page,
            @RequestParam (defaultValue="10") int size,
            @RequestParam (defaultValue="id,asc") String[] sort,
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String desc
    ){
        var orders = new ArrayList<Order>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(Sort.Direction.fromString(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Order(Sort.Direction.fromString(sort[1]), sort[0]));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<CategoryDTO> pageCategories = service.findAllPaginated(pageable, name, desc);
        
        if (pageCategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        
        return ResponseEntity.ok(pageCategories);
    }
}
