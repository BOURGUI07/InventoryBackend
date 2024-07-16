/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import main.dto.CategoryDTO;
import main.dto.StockMvmDTO;
import main.entity.Category;
import main.entity.StockMvm;
import main.mapper.StockMvmMapper;
import main.repo.ProductRepo;
import main.repo.StockMvmRepo;
import main.specification.CategorySpecification;
import main.specification.StockMvmSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    
    @Cacheable(value="StockById", key="#id")
    public StockMvmDTO findById(Integer id){
        var s = repo.findById(id).orElse(null);
        if(s!=null){
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Cacheable(value="AllStocks", key="#root.methodName")
    public List<StockMvmDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value={"AllStocks", "StockById"}, allEntries=true)
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
    @CacheEvict(value={"AllStocks", "StockById"}, allEntries=true)
    public StockMvmDTO update(Integer id,StockMvmDTO x){
        Set<ConstraintViolation<StockMvmDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
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
    @CacheEvict(value={"AllStocks", "StockById"}, allEntries=true)
    public void delete(Integer id){
        if(this.findById(id)!=null){
            repo.deleteById(id);
        }
    }
    
    @CacheEvict(value={"AllStocks", "StockById"}, allEntries=true)
    public void clearCache(){
        
    }
    
    public Page<StockMvmDTO> findAllPaginated(Pageable pageable, String source, String destination, Integer minQty, Integer maxQty, Instant minDate, Instant maxDate){
        Specification<StockMvm> spec = Specification.where(null);
        if(source!=null && !source.isEmpty()){
            spec = spec.and(StockMvmSpecification.sourceLocationContains(source));
        }
        if(destination!=null && !destination.isEmpty()){
            spec = spec.and(StockMvmSpecification.DestinationLocationContains(destination));
        }
        
        if(minQty!=null){
            spec = spec.and(StockMvmSpecification.qtyMoreThan(minQty));
        }
        if(maxQty!=null){
            spec = spec.and(StockMvmSpecification.qtyLessThan(maxQty));
        }
        
        if(minDate!=null){
            spec = spec.and(StockMvmSpecification.yearMoreThan(minDate));
        }
        if(maxDate!=null){
            spec = spec.and(StockMvmSpecification.yearlessThan(maxDate));
        }
        Page<StockMvm> page = repo.findAll(spec, pageable);
        return page.map(mapper::toDTO);
    }
    
    
}
