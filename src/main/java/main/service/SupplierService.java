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
import main.dto.SupplierDTO;
import main.entity.Customer;
import main.entity.Supplier;
import main.mapper.SupplierMapper;
import main.repo.SuppOrderRepo;
import main.repo.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SupplierService {
    @Autowired
    public SupplierService(SupplierMapper mapper, SupplierRepo repo, SuppOrderRepo orderRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.orderRepo = orderRepo;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @PersistenceContext
    private EntityManager em;
    private final SupplierMapper mapper;
    private final SupplierRepo repo;
    private final SuppOrderRepo orderRepo;
    private Validator validator;
    
    public SupplierDTO findById(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            return mapper.toDTO(c);
        }
        return null;
    }
    
    public SupplierDTO findByFirstOrLastName(String firstName, String lastName){
        var q = "SELECT * FROM supplier WHERE firstname= :first OR lastname= :last";
        var c =(Supplier) em.createNativeQuery(q, Customer.class).setParameter("first", firstName).setParameter("last",lastName).getSingleResult();
        try{
            return mapper.toDTO(c);
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    public List<SupplierDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @Transactional
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
    public void delete(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            var list = c.getSuppOrders();
            if(!list.isEmpty()){
                list.forEach(c::removeOrder);
                orderRepo.saveAll(list);
            }
            repo.delete(c);
        }
    }
    
    @Transactional
    public SupplierDTO create(SupplierDTO x){
        Set<ConstraintViolation<SupplierDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var c = mapper.toEntity(x);
        var s = repo.save(c);
        return mapper.toDTO(s);
    }
    
    @Transactional
    public SupplierDTO update(Integer id ,SupplierDTO x){
        Set<ConstraintViolation<SupplierDTO>> violations = validator.validate(x);
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
            if(x.suppOrderIds()!=null){
                c.setSuppOrders(orderRepo.findAllById(x.suppOrderIds()));
            }
            var s = repo.save(c);
            return mapper.toDTO(s);
        }
        return null;
    }
}
