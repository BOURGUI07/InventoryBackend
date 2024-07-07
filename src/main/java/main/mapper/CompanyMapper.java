/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.CompanyDTO;
import main.entity.Company;
import main.repo.ProductRepo;
import main.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CompanyMapper {
    @Autowired
    public CompanyMapper(UserRepo userRepo, ProductRepo productRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }
    private UserRepo userRepo;
    private ProductRepo productRepo;
    public Company toEntity(CompanyDTO x){
        var c = new Company();
        c.setName(x.name());
        if(x.productIds()!=null){
            c.setProducts(productRepo.findAllById(x.productIds()));
        }
        if(x.userIds()!=null){
            c.setUsers(userRepo.findAllById(x.userIds()));
        }
        return c;
    }
    
    public CompanyDTO toDTO(Company c){
        var p = c.getProducts().stream().map(x -> x.getId()).collect(Collectors.toList());
        var u = c.getUsers().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new CompanyDTO(c.getId(),c.getName(),p,u);
    }
}
