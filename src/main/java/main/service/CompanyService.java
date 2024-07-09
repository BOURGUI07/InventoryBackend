/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import main.dto.CompanyDTO;
import main.entity.Company;
import main.mapper.CompanyMapper;
import main.repo.CompanyRepo;
import main.repo.ProductRepo;
import main.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CompanyService {

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public CompanyService(CompanyMapper mapper, CompanyRepo repo, UserRepo userRepo, ProductRepo productRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }
    @PersistenceContext
    private EntityManager em;
    private CompanyMapper mapper;
    private CompanyRepo repo;
    private UserRepo userRepo;
    private ProductRepo productRepo;
    private Validator validator;
    
    public CompanyDTO findByName(String name){
        var q = "SELECT * FROM company WHERE company_name= :x";
        Company c =  (Company)em.createNativeQuery(q, Company.class).setHint("x", name).getSingleResult();
        try{
            return mapper.toDTO(c);
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    public CompanyDTO findById(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            return mapper.toDTO(c);
        }
        return null;
    }
    
    public List<CompanyDTO> findAll(){
        return repo.findAll().stream().map(x -> mapper.toDTO(x)).collect(Collectors.toList());
    }
    
    @Transactional
    public CompanyDTO create(CompanyDTO x){
        Set<ConstraintViolation<CompanyDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var c = mapper.toEntity(x);
        var saved = repo.save(c);
        return mapper.toDTO(saved);
    }
    
    @Transactional
    public CompanyDTO update(Integer id,CompanyDTO x){
        Set<ConstraintViolation<CompanyDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var c= repo.findById(id).orElse(null);
        if(c!=null){
            c.setName(x.name());
            if(x.productIds()!=null){
                c.setProducts(productRepo.findAllById(x.productIds()));
            }
            if(x.userIds()!=null){
                c.setUsers(userRepo.findAllById(x.userIds()));
            }
            var saved = repo.save(c);
            return mapper.toDTO(saved);
        }
        return null;
    }
    
    @Transactional
    public void delete(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            if(!c.getProducts().isEmpty()){
                c.getProducts().forEach(x -> c.removeProduct(x));
            }
            if(!c.getUsers().isEmpty()){
                c.getUsers().forEach(x -> c.removeUser(x));
            }
            repo.delete(c);
        }
    }
}
