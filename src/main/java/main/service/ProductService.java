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
import main.dto.ProductDTO;
import main.entity.Product;
import main.mapper.ProductMapper;
import main.repo.CategRepo;
import main.repo.CompanyRepo;
import main.repo.CustOrderDetailRepo;
import main.repo.ProductRepo;
import main.repo.SalesDetailRepo;
import main.repo.StockMvmRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class ProductService {
    @Autowired
    public ProductService(ProductMapper mapper, ProductRepo productRepo, CategRepo categoryRepo, CompanyRepo repo, CustOrderDetailRepo custRepo, StockMvmRepo stockRepo, SalesDetailRepo salesRepo) {
        this.mapper = mapper;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.repo = repo;
        this.custRepo = custRepo;
        this.stockRepo = stockRepo;
        this.salesRepo = salesRepo;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    private final ProductMapper mapper;
    private final ProductRepo productRepo;
    private final CategRepo categoryRepo;
    private final CompanyRepo repo;
    private final CustOrderDetailRepo custRepo;
    private final StockMvmRepo stockRepo;
    private final SalesDetailRepo salesRepo;
    private Validator validator;
    @PersistenceContext
    private EntityManager em;
    
    
    @Transactional
    public ProductDTO createProduct(ProductDTO x){
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var p = mapper.toEntity(x);
        var savedProduct = productRepo.save(p);
        return mapper.toDTO(savedProduct);
    }
    
    @Transactional
    public ProductDTO updateProduct(Integer id, ProductDTO x){
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        
        var p= productRepo.findById(id).orElse(null);
        if(p!=null){
            p.setCateg(categoryRepo.findById(x.categoryId()).orElse(null));
            p.setCompany(repo.findById(x.companyId()).orElse(null));
            p.setDesc(x.desc());
            p.setName(x.name());
            p.setPic(x.pic());
            p.setPrice(x.price());
            p.setPriceTTC(x.priceTTC());
            p.setVatRate(x.vatRate());
            if(x.custOrderDetailIds()!=null){
                p.setCustOrderDetails(custRepo.findAllById(x.custOrderDetailIds()));
            }
            if(x.salesDetailIds()!=null){
                p.setSalesDetails(salesRepo.findAllById(x.salesDetailIds()));
            }
            if(x.stockMvmIds()!=null){
                p.setStockMvms(stockRepo.findAllById(x.stockMvmIds()));
            }
            var savedP = productRepo.save(p);
            return mapper.toDTO(savedP);
        }
        return null;
    }
    
    @Transactional
    public void deleteProduct(Integer id){
        var p = productRepo.findById(id).orElse(null);
        if(p!=null){
            var list = p.getSalesDetails();
            if(!list.isEmpty()){
                list.stream().forEach(x -> p.removeSalesDetail(x));
                salesRepo.saveAll(list);
            }
            var list1 = p.getStockMvms();
            if(!list1.isEmpty()){
                list1.stream().forEach(x -> p.removeStockMvm(x));
                stockRepo.saveAll(list1);
            }
            var list2 = p.getCustOrderDetails();
            if(!list2.isEmpty()){
                list2.stream().forEach(x -> p.removeCustOrderDetail(x));
                custRepo.saveAll(list2);
            }
            
            productRepo.delete(p);
        }
    } 
    
    public ProductDTO findByName(String name){
        var q = "SELECT * FROM product WHERE product_name= :x";
        try{
            return mapper.toDTO((Product)  em.createNativeQuery(q, Product.class).setParameter("x", name).getSingleResult());
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    public ProductDTO findByid(Integer id){
        var p = productRepo.findById(id).orElse(null);
        if(p!=null){
            return mapper.toDTO(p);
        }
        return null;
    }
    
    public List<ProductDTO> findAll(){
        return productRepo.findAll().stream().map(x -> mapper.toDTO(x)).collect(Collectors.toList());
    }
}
