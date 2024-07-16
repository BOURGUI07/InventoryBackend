/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.specification;

import main.entity.Company;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author hp
 */
public class CompanySpecification {
    public static Specification<Company> nameContains(String name){
        return (root, query, criteriaBuilder) -> 
                criteriaBuilder.like(root.get(name), "%" + name + "%");
    }
}
