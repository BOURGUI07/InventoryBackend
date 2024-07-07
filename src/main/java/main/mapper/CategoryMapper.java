/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.CategoryDTO;
import main.entity.Category;
import main.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CategoryMapper {
    @Autowired
    public CategoryMapper(ProductRepo repo) {
        this.repo = repo;
    }
    
    private ProductRepo repo;
    
    public Category toEntity(CategoryDTO x){
        var c = new Category();
        c.setName(x.name());
        c.setDesc(x.desc());
        if(x.productIds()!=null){
            c.setProducts(repo.findAllById(x.productIds()));
        }
        return c;
    }
    
    public CategoryDTO toDTO(Category c){
        var list = c.getProducts().stream().map(p -> p.getId()).collect(Collectors.toList());
        return new CategoryDTO(c.getId(),c.getName(),c.getDesc(),list);
    }
}
