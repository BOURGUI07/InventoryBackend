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
import main.dto.SalesDTO;
import main.entity.Sales;
import main.entity.SalesDetailId;
import main.mapper.SalesMapper;
import main.repo.SalesDetailRepo;
import main.repo.SalesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SalesService {

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public SalesService(SalesMapper mapper, SalesRepo repo, SalesDetailRepo detailRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.detailRepo = detailRepo;
    }
    @PersistenceContext
    private EntityManager em;
    private final SalesMapper mapper;
    private final SalesRepo repo;
    private final SalesDetailRepo detailRepo;
    private Validator validator;
    
    @Transactional
    @CacheEvict(value={"SalesById", "SalesByCode", "AllSales"}, allEntries=true)
    public SalesDTO create(SalesDTO x){
        Set<ConstraintViolation<SalesDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var s = mapper.toEntity(x);
        var ss = repo.save(s);
        return mapper.toDTO(ss);
    }
    
    @Transactional
    @CacheEvict(value={"SalesById", "SalesByCode", "AllSales"}, allEntries=true)
    public SalesDTO update(Integer id, SalesDTO x){
        Set<ConstraintViolation<SalesDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var s = repo.findById(id).orElse(null);
        if(s!=null){
            s.setCode(x.code());
            if(x.salesDetailIds()!=null){
                s.setSalesDetails(detailRepo.findAllById(x.salesDetailIds()));
            }
            var ss = repo.save(s);
            return mapper.toDTO(ss);
        }
        return null;
    }
    
    @Transactional
    @CacheEvict(value={"SalesById", "SalesByCode", "AllSales"}, allEntries=true)
    public void addDetail(Integer id, Integer productid){
        var s = repo.findById(id).orElse(null);
        var detailid = new SalesDetailId(productid, id);
        if(s!=null){
            var d = detailRepo.findById(detailid).orElse(null);
            if(d!=null){
                s.addDetail(d);
                repo.save(s);
                detailRepo.save(d);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"SalesById", "SalesByCode", "AllSales"}, allEntries=true)
    public void removeDetail(Integer id, Integer productid){
        var s = repo.findById(id).orElse(null);
        var detailid = new SalesDetailId(productid, id);
        if(s!=null){
            var d = detailRepo.findById(detailid).orElse(null);
            if(d!=null){
                s.removeDetail(d);
                repo.save(s);
                detailRepo.save(d);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"SalesById", "SalesByCode", "AllSales"}, allEntries=true)
    public void delete(Integer id){
        var s = repo.findById(id).orElse(null);
        if(s!=null){
            var list = s.getSalesDetails();
            if(!list.isEmpty()){
                list.forEach(s::removeDetail);
                detailRepo.saveAll(list);
            }
            repo.delete(s);
        }
    }
    
    @Cacheable(value="SalesById", key="#id")
    public SalesDTO findById(Integer id){
        var s = repo.findById(id).orElse(null);
        if(s!=null){
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Cacheable(value="AllSales", key="#root.methodName")
    public List<SalesDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @Cacheable(value="SalesByCode", key="#code")
    public SalesDTO findByCode(String code){
        var q = "SELECT * FROM sales WHERE sales_code= :x";
        try{
            return mapper.toDTO((Sales) em.createNativeQuery(q, Sales.class).setParameter("x", code).getSingleResult() );
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    @CacheEvict(value={"SalesById", "SalesByCode", "AllSales"}, allEntries=true)
    public void clearCache(){
        
    }
}
