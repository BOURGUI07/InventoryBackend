/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import main.dto.CompanyDTO;
import main.dto.ProductDTO;
import main.dto.StockMvmDTO;
import main.handler.RessourceNotFoundException;
import main.service.ProductService;
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
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {
    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }
    private final ProductService service;
    
    @Operation(summary="Get All products", description="Get a List of products")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found List of products"),
        @ApiResponse(responseCode="204", description="Found an empty list of products")
    })
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findAll(){
        var s = service.findAll();
        if(s.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get product by Id", description="Return a single product")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The product"),
        @ApiResponse(responseCode="404", description="product not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Integer id){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        var s = service.findById(id);
        if(s==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(s);
    }
    
    @Operation(summary="Get product by Name", description="Return a single product")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="Found Sucessfully The product"),
        @ApiResponse(responseCode="404", description="product not found"),
        @ApiResponse(responseCode="400", description="The input name is blank")
    })
    @GetMapping("/products/name/{name}")
    public ResponseEntity<ProductDTO> findByName(@PathVariable String name){
        var c = service.findByName(name);
        if(name.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(c==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(c);
    }
    
    @Operation(summary="Create a new product")
    @ApiResponse(responseCode="201", description="product created successfully")
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO x){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(x));
    }
    
    @Operation(summary="Update a product")
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="product Updated Sucessfully"),
        @ApiResponse(responseCode="404", description="product not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id, @Valid @RequestBody ProductDTO x){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.update(id, x));
    }
    
    @Operation(summary="Delete a product")
    @ApiResponses(value={
        @ApiResponse(responseCode="204", description="product Deleted Sucessfully"),
        @ApiResponse(responseCode="404", description="product not found"),
        @ApiResponse(responseCode="400", description="The input id is non-valid")
    })
    @DeleteMapping("/products/{id}")
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
    
    //GET /api/products/search?name=example&categoryId=1&minPrice=10.00&maxPrice=100.00
    @Operation(summary="search products based on category id and min/max price")
    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDTO>> search(
            @RequestParam(required=false) Integer categoryId,
            @RequestParam(required=false) BigDecimal minPrice,
            @RequestParam(required=false) BigDecimal maxPrice
    ){
        return ResponseEntity.ok(service.searchProducts(categoryId, minPrice, maxPrice));
    }
    
    @Operation(summary="Get product stock movement history")
    @GetMapping("/products/stock/{id}")
    public ResponseEntity<List<StockMvmDTO>> getproductHistory(@PathVariable Integer id){
        if(id<0 || id>service.findAll().size()){
            throw new RessourceNotFoundException("Id non-valid");
        }
        if(service.findById(id)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(service.getProductStockHistory(id));
    }
    
    @Operation(summary = "Get paginated list of products", description = "Retrieve a paginated list of products with optional search and sorting capabilities.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found paginated list of products"),
        @ApiResponse(responseCode = "204", description = "No products found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductDTO>> findAllPaginated(
            @RequestParam (defaultValue="0") int page,
            @RequestParam (defaultValue="10") int size,
            @RequestParam (defaultValue="id,asc") String[] sort,
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String desc,
            @RequestParam (required = false) BigDecimal minPrice,
            @RequestParam (required = false) BigDecimal maxPrice
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
        Page<ProductDTO> pageProducts = service.findAllPaginated(pageable, name,desc,minPrice,maxPrice);
        
        if (pageProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        
        return ResponseEntity.ok(pageProducts);
    }
}
