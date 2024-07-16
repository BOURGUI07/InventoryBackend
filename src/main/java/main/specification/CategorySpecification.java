/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.specification;

import main.entity.Category;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author hp
 */
public class CategorySpecification {
    public static Specification<Category> nameContains(String name){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.like(root.get(name), "%" + name + "%");
    }
    
    public static Specification<Category> descContains(String desc){
        return (root, query,criteriaBuilder) -> 
                criteriaBuilder.like(root.get(desc), "%" + desc + "%");
    }
}
