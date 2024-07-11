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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private final CompanyMapper mapper;
    private final CompanyRepo repo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private Validator validator;
    
    @Cacheable(value="CompanyByName", key="#name")
    public CompanyDTO findByName(String name){
        var q = "SELECT * FROM company WHERE company_name= :x";
        Company c =  (Company)em.createNativeQuery(q, Company.class).setParameter("x", name).getSingleResult();
        try{
            return mapper.toDTO(c);
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    @Cacheable(value="CompanyById", key="#id")
    public CompanyDTO findById(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            return mapper.toDTO(c);
        }
        return null;
    }
    
    @Cacheable(value="AllCompanies", key="#root.methodName")
    public List<CompanyDTO> findAll(){
        return repo.findAll().stream().map(x -> mapper.toDTO(x)).collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
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
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
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
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
    public void delete(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            var list = c.getProducts();
            var list1 = c.getUsers();
            if(!list.isEmpty()){
                list.forEach(x -> c.removeProduct(x));
                productRepo.saveAll(list);
            }
            if(!list1.isEmpty()){
                list1.forEach(x -> c.removeUser(x));
                userRepo.saveAll(list1);
            }
            repo.delete(c);
        }
    }
    
    @Transactional
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
    public void addProductToCompany(Integer companyId, Integer productId){
        var c = repo.findById(companyId).orElse(null);
        if(c!=null){
            var p = productRepo.findById(productId).orElse(null);
            if(p!=null){
                c.addProduct(p);
                repo.save(c);
                productRepo.save(p);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
    public void removeProductFromCompany(Integer companyid, Integer productId){
        var c = repo.findById(companyid).orElse(null);
        if(c!=null){
            var p = productRepo.findById(productId).orElse(null);
            if(p!=null){
                c.removeProduct(p);
                repo.save(c);
                productRepo.save(p);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
    public void addUserToCompany(Integer companyId, Integer userid){
        var c = repo.findById(companyId).orElse(null);
        if(c!=null){
            var u = userRepo.findById(userid).orElse(null);
            if(u!=null){
                c.addUser(u);
                repo.save(c);
                userRepo.save(u);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
    public void removeUserFromCompany(Integer companyid, Integer userid){
        var c = repo.findById(companyid).orElse(null);
        if(c!=null){
            var u = userRepo.findById(userid).orElse(null);
            if(u!=null){
                c.removeUser(u);
                repo.save(c);
                userRepo.save(u);
            }
        }
    }
    
    @CacheEvict(value={"AllCompanies","CompanyById","CompanyByName"}, allEntries=true)
    public void clearCache(){
        
    }
}
