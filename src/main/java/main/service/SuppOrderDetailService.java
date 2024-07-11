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
import main.dto.SuppOrderDetailDTO;
import main.entity.SuppOrderDetailId;
import main.mapper.SuppOrderDetailMapper;
import main.repo.ProductRepo;
import main.repo.SuppOrderDetailRepo;
import main.repo.SuppOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SuppOrderDetailService {
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public SuppOrderDetailService(SuppOrderDetailRepo repo, SuppOrderRepo salesRepo, ProductRepo productRepo, SuppOrderDetailMapper mapper) {
        this.repo = repo;
        this.orderRepo = salesRepo;
        this.productRepo = productRepo;
        this.mapper = mapper;
    }
    private final SuppOrderDetailRepo repo;
    private final SuppOrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final SuppOrderDetailMapper mapper;
    private Validator validator;
    
    @Transactional
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public SuppOrderDetailDTO create(SuppOrderDetailDTO x){
        Set<ConstraintViolation<SuppOrderDetailDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var d = mapper.toEntity(x);
        var s = repo.save(d);
        return mapper.toDTO(s);
    }
    
    @Transactional
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public SuppOrderDetailDTO update(Integer productid, Integer salesid,SuppOrderDetailDTO x ){
        var id = new SuppOrderDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            d.setProduct(productRepo.findById(x.productId()).orElse(null));
            d.setSuppOrder(orderRepo.findById(x.suppOrderId()).orElse(null));
            d.setQuantity(x.quantity());
            var s = repo.save(d);
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Transactional
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public void delete(Integer productid, Integer salesid){
        var id = new SuppOrderDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            repo.delete(d);
        }
    }
    
    @Cacheable(value="DetailById", key="{#productid ,#salesid}")
    public SuppOrderDetailDTO findById(Integer productid, Integer salesid){
        var id = new SuppOrderDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            return mapper.toDTO(d);
        }
        return null;
    }
    
    @Cacheable(value="AllDetails", key="#root.methodName")
    public List<SuppOrderDetailDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @CacheEvict(value={"AllDetails", "DetailById"}, allEntries=true)
    public void clearCache(){
        
    }
}
