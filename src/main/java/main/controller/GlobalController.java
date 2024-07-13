/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controller;

import java.time.Instant;
import java.util.List;
import main.dto.BestCustomerDTO;
import main.dto.CompanyProductsDTO;
import main.dto.CustomersNoOrdersDTO;
import main.dto.PopularProductDTO;
import main.dto.ProductHighestQtyDTO;
import main.dto.ProductQtyDTO;
import main.dto.SalesByCategoryDTO;
import main.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hp
 */
@RestController
@RequestMapping("/api/global")
public class GlobalController {
    @Autowired
    public GlobalController(GlobalService service) {
        this.service = service;
    }
    private final GlobalService service;
    
    @GetMapping("/popular-products")
    public ResponseEntity<List<PopularProductDTO>> popularProducts(
            @RequestParam(required=false) Instant startdate,
            @RequestParam(required=false) Instant enddate){
        if(startdate.isAfter(enddate) || enddate.isBefore(startdate)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(service.getMostPopularProducts(startdate, enddate));
    }
    
    @GetMapping("/best-customers")
    public ResponseEntity<List<BestCustomerDTO>> bestCustomers(
            @RequestParam(required=false) Instant startdate,
            @RequestParam(required=false) Instant enddate){
        if(startdate.isAfter(enddate) || enddate.isBefore(startdate)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(service.getBestCustomers(startdate, enddate));
    }
    
    @GetMapping("/products-with-highest-qty")
    public ResponseEntity<List<ProductHighestQtyDTO>> productWithHighestQty(){
        return ResponseEntity.ok(service.productsWithHighestQty());
    }
    
    @GetMapping("/sales-by-category")
    public ResponseEntity<List<SalesByCategoryDTO>> salesByCategory(){
        return ResponseEntity.ok(service.salesByCategory());
    }
    
    @GetMapping("/customers-with-no-orders")
    public ResponseEntity<List<CustomersNoOrdersDTO>> customersWithNoOders(){
        return ResponseEntity.ok(service.customersWithNoOrders());
    }
    
    @GetMapping("/products-qty-tbelow")
    public ResponseEntity<List<ProductQtyDTO>> productsQtyBelow(@RequestParam Integer qty){
        if(qty<0){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return ResponseEntity.ok(service.productsQtyBelow(qty));
    }
    
    @GetMapping("/products-belonging-to-company")
    public ResponseEntity<List<CompanyProductsDTO>> productsBelongingToCompany(@RequestParam String name){
        return ResponseEntity.ok(service.productsBelongingToCompany(name));
    }
}
