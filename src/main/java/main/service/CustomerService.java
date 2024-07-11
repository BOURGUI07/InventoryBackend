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
import main.dto.CustomerDTO;
import main.entity.Customer;
import main.mapper.CustomerMapper;
import main.repo.CustOrderRepo;
import main.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CustomerService {
    @Autowired
    public CustomerService(CustomerMapper mapper, CustomerRepo repo, CustOrderRepo orderRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.orderRepo = orderRepo;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @PersistenceContext
    private EntityManager em;
    private final CustomerMapper mapper;
    private final CustomerRepo repo;
    private final CustOrderRepo orderRepo;
    private Validator validator;
    
    @Cacheable(value="CustomerById", key="#id")
    public CustomerDTO findById(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            return mapper.toDTO(c);
        }
        return null;
    }
    
    @Cacheable(value="CustomerByFirstOrLastName", key="{#firstName , #lastName}")
    public CustomerDTO findByFirstOrLastName(String firstName, String lastName){
        var q = "SELECT * FROM customer WHERE firstname= :first OR lastname= :last";
        var c =(Customer) em.createNativeQuery(q, Customer.class).setParameter("first", firstName).setParameter("last",lastName).getSingleResult();
        try{
            return mapper.toDTO(c);
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    @Cacheable(value="AllCustomers", key="#root.methodName")
    public List<CustomerDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @CacheEvict(value={"AllCustomers","CustomerByFirstOrLastName","CustomerById"}, allEntries=true)
    public void clearCache(){
        
    }
    
    @Transactional
    @CacheEvict(value={"AllCustomers","CustomerByFirstOrLastName","CustomerById"}, allEntries=true)
    public void addOrder(Integer id, Integer orderid){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            var o = orderRepo.findById(orderid).orElse(null);
            if(o!=null){
                c.addOrder(o);
                repo.save(c);
                orderRepo.save(o);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"AllCustomers","CustomerByFirstOrLastName","CustomerById"}, allEntries=true)
    public void removeOrder(Integer id, Integer orderid){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            var o = orderRepo.findById(orderid).orElse(null);
            if(o!=null){
                c.removeOrder(o);
                repo.save(c);
                orderRepo.save(o);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"AllCustomers","CustomerByFirstOrLastName","CustomerById"}, allEntries=true)
    public void delete(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            var list = c.getCustOrders();
            if(!list.isEmpty()){
                list.forEach(c::removeOrder);
                orderRepo.saveAll(list);
            }
            repo.delete(c);
        }
    }
    
    @Transactional
    @CacheEvict(value={"AllCustomers","CustomerByFirstOrLastName","CustomerById"}, allEntries=true)
    public CustomerDTO create(CustomerDTO x){
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var c = mapper.toEntity(x);
        var s = repo.save(c);
        return mapper.toDTO(s);
    }
    
    @Transactional
    @CacheEvict(value={"AllCustomers","CustomerByFirstOrLastName","CustomerById"}, allEntries=true)
    public CustomerDTO update(Integer id ,CustomerDTO x){
        Set<ConstraintViolation<CustomerDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            c.setAddress(x.address());
            c.setEmail(x.email());
            c.setFirstName(x.firstName());
            c.setLastName(x.lastName());
            c.setPhone(x.phone());
            c.setPic(x.pic());
            if(x.custOrderIds()!=null){
                c.setCustOrders(orderRepo.findAllById(x.custOrderIds()));
            }
            var s = repo.save(c);
            return mapper.toDTO(s);
        }
        return null;
    }
}
