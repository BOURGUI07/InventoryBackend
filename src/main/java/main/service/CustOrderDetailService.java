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
import main.dto.CustOrderDetailDTO;
import main.entity.CustOrderDetailId;
import main.mapper.CustOrderDetailMapper;
import main.repo.CustOrderDetailRepo;
import main.repo.CustOrderRepo;
import main.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CustOrderDetailService {
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public CustOrderDetailService(CustOrderDetailRepo repo, CustOrderRepo orderRepo, ProductRepo productRepo, CustOrderDetailMapper mapper) {
        this.repo = repo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.mapper = mapper;
    }
    private final CustOrderDetailRepo repo;
    private final CustOrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final CustOrderDetailMapper mapper;
    private Validator validator;
    
    @Transactional
    public CustOrderDetailDTO create(CustOrderDetailDTO x){
        Set<ConstraintViolation<CustOrderDetailDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var d = mapper.toEntity(x);
        var s = repo.save(d);
        return mapper.toDTO(s);
    }
    
    @Transactional
    public CustOrderDetailDTO update(Integer productid, Integer salesid,CustOrderDetailDTO x ){
        var id = new CustOrderDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            d.setProduct(productRepo.findById(x.productId()).orElse(null));
            d.setCustOrder(orderRepo.findById(x.custOrderId()).orElse(null));
            d.setQuantity(x.quantity());
            var s = repo.save(d);
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Transactional
    public void delete(Integer productid, Integer salesid){
        var id = new CustOrderDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            repo.delete(d);
        }
    }
    
    public CustOrderDetailDTO findById(Integer productid, Integer salesid){
        var id = new CustOrderDetailId(productid, salesid);
        var d = repo.findById(id).orElse(null);
        if(d!=null){
            return mapper.toDTO(d);
        }
        return null;
    }
    
    public List<CustOrderDetailDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
