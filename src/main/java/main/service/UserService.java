/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import main.dto.StockMvmDTO;
import main.dto.UserDTO;
import main.mapper.UserMapper;
import main.repo.CompanyRepo;
import main.repo.RoleRepo;
import main.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class UserService {

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public UserService(UserMapper mapper, UserRepo repo, RoleRepo roleRepo, CompanyRepo companyRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.roleRepo = roleRepo;
        this.companyRepo = companyRepo;
    }
    private final UserMapper mapper;
    private final UserRepo repo;
    private final RoleRepo roleRepo;
    private final CompanyRepo companyRepo;
    private Validator validator;
    
    @Transactional
    public UserDTO create(UserDTO x){
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var u = mapper.toEntity(x);
        var s = repo.save(u);
        return mapper.toDTO(s);
    }
    
    @Transactional
    public UserDTO update(Integer id, UserDTO x){
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var  u = repo.findById(id).orElse(null);
        if(u!=null){
            u.setAddress(x.address());
            u.setBirthDate(x.birthdate());
            u.setLastName(x.lastName());
            u.setCompany(companyRepo.findById(x.companyId()).orElse(null));
            u.setEmail(x.email());
            u.setFirstName(x.firstName());
            u.setPassword(x.password());
            u.setPic(x.pic());
            if(x.roleIds()!=null){
                u.setRoles(roleRepo.findAllById(x.roleIds()));
            }
            var s = repo.save(u);
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Transactional
    public void delete(Integer id){
        var u = repo.findById(id).orElse(null);
        if(u!=null){
            var list = u.getRoles();
            if(!list.isEmpty()){
                list.forEach(u::removeRole);
                roleRepo.saveAll(list);
            }
            repo.delete(u);
        }
    }
    
    public UserDTO findById(Integer id){
        var u = repo.findById(id).orElse(null);
        if(u!=null){
            return mapper.toDTO(u);
        }
        return null;
    }
    
    public List<UserDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
