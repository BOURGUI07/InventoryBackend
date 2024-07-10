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
import main.mapper.StockMvmMapper;
import main.repo.ProductRepo;
import main.repo.StockMvmRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class StockMvmService {
    @Autowired
    public StockMvmService(StockMvmMapper mmaper, StockMvmRepo repo, ProductRepo productRepo) {
        this.mapper = mmaper;
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    private final StockMvmMapper mapper;
    private final StockMvmRepo repo;
    private final ProductRepo productRepo;
    private Validator validator;
    
    public StockMvmDTO findById(Integer id){
        var s = repo.findById(id).orElse(null);
        if(s!=null){
            return mapper.toDTO(s);
        }
        return null;
    }
    
    public List<StockMvmDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @Transactional
    public StockMvmDTO create(StockMvmDTO x){
        Set<ConstraintViolation<StockMvmDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var s = mapper.toEntity(x);
        var ss = repo.save(s);
        return mapper.toDTO(ss);
    }
    
    @Transactional
    public StockMvmDTO update(StockMvmDTO x, Integer id){
        var s = repo.findById(id).orElse(null);
        if(s!=null){
            s.setDestinationLocation(x.destination());
            s.setSourceLocation(x.source());
            s.setQty(x.qty());
            s.setProduct(productRepo.findById(x.productId()).orElse(null));
            s.setMovementType(x.type());
            s.setMovementDate(x.date());
            var ss = repo.save(s);
            return mapper.toDTO(ss);
        }
        return null;
    }
    
    @Transactional
    public void delete(Integer id){
        if(this.findById(id)!=null){
            repo.deleteById(id);
        }
    }
}
