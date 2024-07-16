/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.specification;

import java.time.Instant;
import main.entity.StockMvm;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author hp
 */
public class StockMvmSpecification {
    public static Specification<StockMvm> sourceLocationContains(String source){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.like(root.get(source), "%" + source + "%");
    }
    
    public static Specification<StockMvm> DestinationLocationContains(String destination){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.like(root.get(destination), "%" + destination + "%");
    }
    
    public static Specification<StockMvm> qtyMoreThan(Integer qty){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.greaterThanOrEqualTo(root.get("qty"),qty);
    }
    
    public static Specification<StockMvm> qtyLessThan(Integer qty){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.lessThanOrEqualTo(root.get("qty"),qty);
    }
    
    public static Specification<StockMvm> yearMoreThan(Instant date){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.greaterThanOrEqualTo(root.get("movementDate"),date);
    }
    
    public static Specification<StockMvm> yearlessThan(Instant date){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.lessThanOrEqualTo(root.get("movementDate"),date);
    }
}
