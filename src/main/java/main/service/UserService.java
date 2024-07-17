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
import main.dto.UserDTO;
import main.mapper.UserMapper;
import main.repo.CompanyRepo;
import main.repo.RoleRepo;
import main.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value={"UserById", "AllUsers"}, allEntries=true)
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
    @CacheEvict(value={"UserById", "AllUsers"}, allEntries=true)
    public UserDTO update(Integer id, UserDTO x){
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var  u = repo.findById(id).orElse(null);
        if(u!=null){
            u.setCompany(companyRepo.findById(x.companyId()).orElse(null));
            u.setEmail(x.email());
            u.setUsername(x.username());
            u.setPassword(x.password());
            u.setEnabled(x.enabled());
            if(x.roleIds()!=null){
                u.setRoles(roleRepo.findAllById(x.roleIds()));
            }
            var s = repo.save(u);
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Transactional
    @CacheEvict(value={"UserById", "AllUsers"}, allEntries=true)
    public void delete(Integer id){
        var u = repo.findById(id).orElse(null);
        if(u!=null){
            repo.delete(u);
        }
    }
    
    @Cacheable(value="UserById", key="#id")
    public UserDTO findById(Integer id){
        var u = repo.findById(id).orElse(null);
        if(u!=null){
            return mapper.toDTO(u);
        }
        return null;
    }
    
    @Cacheable(value="AllUsers", key="#root.methodName")
    public List<UserDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @CacheEvict(value={"UserById", "AllUsers"}, allEntries=true)
    public void clearCache(){
        
    }
    
    public Boolean existsByUsername(String username){
        return repo.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email){
        return repo.existsByEmail(email);
    }
}
