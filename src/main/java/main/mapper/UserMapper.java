/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import main.dto.UserDTO;
import main.entity.User;
import main.repo.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class UserMapper {
    @Autowired
    public UserMapper(CompanyRepo repo) {
        this.repo = repo;
    }
    private CompanyRepo repo;
    
    public User toEntity(UserDTO x){
        var u = new User();
        u.setName(x.name());
        u.setCompany(repo.findById(x.companyId()).orElse(null));
        return u;
    }
    
    public UserDTO toDTO(User u){
        return new UserDTO(u.getId(),u.getName(),u.getCompany().getId());
    }
}
