/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.UserDTO;
import main.entity.User;
import main.repo.CompanyRepo;
import main.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class UserMapper {
    @Autowired
    public UserMapper(CompanyRepo repo, RoleRepo roleRepo) {
        this.repo = repo;
        this.roleRepo=roleRepo;
    }
    private CompanyRepo repo;
    private RoleRepo roleRepo;
    
    public User toEntity(UserDTO x){
        var u = new User();
        u.setAddress(x.address());
        u.setBirthDate(x.birthdate());
        u.setCompany(repo.findById(x.companyId()).orElse(null));
        u.setEmail(x.email());
        u.setPic(x.pic());
        u.setLastName(x.lastName());
        u.setFirstName(x.firstName());
        u.setPassword(x.password());
        if(x.roleIds()!=null){
            u.setRoles(roleRepo.findAllById(x.roleIds()));
        }
        return u;
    }
    
    public UserDTO toDTO(User u){
        var list = u.getRoles().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new UserDTO(u.getId(),u.getFirstName(),u.getLastName(),u.getBirthDate(),u.getPassword(),u.getAddress(),u.getEmail(),u.getPic(),u.getCompany().getId(),list);
    }
}
