/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.specification;

import java.math.BigDecimal;
import main.entity.Product;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author hp
 */
public class ProductSpecification {
    public static Specification<Product> nameContains(String name){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.like(root.get(name), "%" + name + "%");
    }
    
    public static Specification<Product> descContains(String desc){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.like(root.get(desc), "%" + desc + "%");
    }
    
    public static Specification<Product> priceMoreThan(BigDecimal price){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"),price);
    }
    
    public static Specification<Product> priceLessThan(BigDecimal price){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.lessThanOrEqualTo(root.get("price"),price);
    }
}
