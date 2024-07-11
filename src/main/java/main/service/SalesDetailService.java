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
import main.dto.SalesDetailDTO;
import main.entity.SalesDetailId;
import main.mapper.SalesDetailMapper;
import main.repo.ProductRepo;
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
public class SalesDetailService {

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public SalesDetailService(SalesDetailRepo repo, SalesRepo salesRepo, ProductRepo productRepo, SalesDetailMapper mapper) {
        this.repo = repo;
        this.salesRepo = salesRepo;
        this.productRepo = productRepo;
        this.mapper = mapper;
    }
    private final SalesDetailRepo repo;
    private final SalesRepo salesRepo;
    private final ProductRepo productRepo;
    private final SalesDetailMapper mapper;
    private Validator validator;
    
    @Transactional
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public SalesDetailDTO create(SalesDetailDTO x){
        Set<ConstraintViolation<SalesDetailDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var d = mapper.toEntity(x);
        var s = repo.save(d);
        return mapper.toDTO(s);
    }
    
    @Transactional
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public SalesDetailDTO update(Integer productid, Integer salesid,SalesDetailDTO x ){
        var id = new SalesDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            d.setProduct(productRepo.findById(x.productId()).orElse(null));
            d.setSales(salesRepo.findById(x.salesId()).orElse(null));
            d.setQuantity(x.quantity());
            var s = repo.save(d);
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Transactional
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public void delete(Integer productid, Integer salesid){
        var id = new SalesDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            repo.delete(d);
        }
    }
    
    @Cacheable(value="DetailById", key="{#productid ,#salesid}")
    public SalesDetailDTO findById(Integer productid, Integer salesid){
        var id = new SalesDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            return mapper.toDTO(d);
        }
        return null;
    }
    
    @Cacheable(value="AllDetails", key="#root.methodName")
    public List<SalesDetailDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public void clearCache(){
        
    }
}
