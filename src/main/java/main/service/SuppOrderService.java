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
import main.dto.SuppOrderDTO;
import main.entity.SuppOrder;
import main.entity.SuppOrderDetailId;
import main.mapper.SuppOrderMapper;
import main.repo.SuppOrderDetailRepo;
import main.repo.SuppOrderRepo;
import main.repo.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SuppOrderService {
    
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public SuppOrderService(SuppOrderMapper mapper, SuppOrderRepo repo, SupplierRepo suppRepo, SuppOrderDetailRepo detailRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.suppRepo = suppRepo;
        this.detailRepo = detailRepo;
    }
    private Validator validator;
    @PersistenceContext
    private EntityManager em;
    private final SuppOrderMapper mapper;
    private final SuppOrderRepo repo;
    private final SupplierRepo suppRepo;
    private final SuppOrderDetailRepo detailRepo;
    
    @Cacheable(value="OrderByCode", key="#code")
    public SuppOrderDTO findByCode(String code){
        var q = "SELECT * FROM supp_order WHERE order_code= :x";
        var o =  (SuppOrder)em.createNativeQuery(q, SuppOrder.class).setHint("x", code).getSingleResult();
        try{
            return mapper.toDTO(o);
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    @Cacheable(value="OrderById", key="#id")
    public SuppOrderDTO findById(Integer id){
        var o = repo.findById(id).orElse(null);
        if(o!=null){
            return mapper.toDTO(o);
        }
        return null;
    }
    
    @Cacheable(value="AllOrders", key="#root.methodName")
    public List<SuppOrderDTO> findAll(){
        return repo.findAll().stream().map(x -> mapper.toDTO(x)).collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value={"OrderById","AllOrders","OrderByCode"}, allEntries=true)
    public SuppOrderDTO create(SuppOrderDTO x){
        Set<ConstraintViolation<SuppOrderDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var o = mapper.toEntity(x);
        var s = repo.save(o);
        return mapper.toDTO(s);
    }
    
    @Transactional
    @CacheEvict(value={"OrderById","AllOrders","OrderByCode"}, allEntries=true)
    public SuppOrderDTO update(Integer id, SuppOrderDTO x){
        Set<ConstraintViolation<SuppOrderDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var o = repo.findById(id).orElse(null);
        if(o!=null){
            o.setCode(x.code());
            o.setSupplier(suppRepo.findById(x.supplierId()).orElse(null));
            o.setOrderDate(x.date());
            if(x.suppOrderDetailIds()!=null){
                o.setSuppOrderDetails(detailRepo.findAllById(x.suppOrderDetailIds()));
            }
            var s = repo.save(o);
            return mapper.toDTO(s);
        }
        return null;
    }
    
    @Transactional
    @CacheEvict(value={"OrderById","AllOrders","OrderByCode"}, allEntries=true)
    public void delete(Integer id){
        var o = repo.findById(id).orElse(null);
        if(o!=null){
            var list = o.getSuppOrderDetails();
            if(!list.isEmpty()){
                list.forEach(x -> o.removeDetail(x));
                detailRepo.saveAll(list);
            }
            repo.delete(o);
        }
    }
    
    @Transactional
    @CacheEvict(value={"OrderById","AllOrders","OrderByCode"}, allEntries=true)
    public void adddetailToOrder(Integer orderid, Integer productId){
        var detailId = new SuppOrderDetailId(productId,orderid);
        var o = repo.findById(orderid).orElse(null);
        if(o!=null){
            var detail = detailRepo.findById(detailId).orElse(null);
            if(detail!=null){
                o.addDetail(detail);
                repo.save(o);
                detailRepo.save(detail);
            }
        }
    }
    
    @Transactional
    @CacheEvict(value={"OrderById","AllOrders","OrderByCode"}, allEntries=true)
    public void removeDetailFromOrder(Integer orderid, Integer productId){
        var detailId = new SuppOrderDetailId(productId,orderid);
        var o = repo.findById(orderid).orElse(null);
        if(o!=null){
            var detail = detailRepo.findById(detailId).orElse(null);
            if(detail!=null){
                o.removeDetail(detail);
                repo.save(o);
                detailRepo.save(detail);
            }
        }
    }
    
    @CacheEvict(value={"OrderById","AllOrders","OrderByCode"}, allEntries=true)
    public void clearCache(){
        
    }
}
