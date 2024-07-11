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
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import main.dto.ProductDTO;
import main.dto.StockMvmDTO;
import main.entity.Product;
import main.mapper.ProductMapper;
import main.mapper.StockMvmMapper;
import main.repo.CategRepo;
import main.repo.CompanyRepo;
import main.repo.CustOrderDetailRepo;
import main.repo.ProductRepo;
import main.repo.SalesDetailRepo;
import main.repo.StockMvmRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class ProductService {
    @Autowired
    public ProductService(ProductMapper mapper, ProductRepo productRepo, CategRepo categoryRepo, CompanyRepo repo, CustOrderDetailRepo custRepo, StockMvmRepo stockRepo, SalesDetailRepo salesRepo, StockMvmMapper mapper1) {
        this.mapper = mapper;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.repo = repo;
        this.custRepo = custRepo;
        this.stockRepo = stockRepo;
        this.salesRepo = salesRepo;
        this.mapper1 = mapper1;
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
    private final StockMvmMapper mapper1;
    private Validator validator;
    @PersistenceContext
    private EntityManager em;
    
    
    @Transactional
    @CacheEvict(value={"ProductById", "ProductByName", "AllProducts"}, allEntries=true)
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
    @CacheEvict(value={"ProductById", "ProductByName", "AllProducts"}, allEntries=true)
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
    @CacheEvict(value={"ProductById", "ProductByName", "AllProducts"}, allEntries=true)
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
    
    @Cacheable(value="ProductByName", key="#name")
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
    
    @Cacheable(value="ProductById", key="#id")
    public ProductDTO findByid(Integer id){
        var p = productRepo.findById(id).orElse(null);
        if(p!=null){
            return mapper.toDTO(p);
        }
        return null;
    }
    
    @Cacheable(value="AllProducts", key="#root.methodName")
    public List<ProductDTO> findAll(){
        return productRepo.findAll().stream().map(x -> mapper.toDTO(x)).collect(Collectors.toList());
    }
    
    //Implement a method to search for products with multiple filters like name, category, price range, etc.
    public List<ProductDTO> searchProducts(String name, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice){
        return productRepo.findAll().stream().filter(p -> 
                p.getName().equals(name) 
                && p.getCateg().getId().equals(categoryId)
                && p.getPrice().compareTo(maxPrice)<=0
                && p.getPrice().compareTo(minPrice)>=0).map(mapper::toDTO).collect(Collectors.toList());       
        
    }
    
    //Implement a method to retrieve the history of stock movements for a given product.
    public List<StockMvmDTO> getProductStockHistory(Integer id){
        var  p = productRepo.findById(id).orElse(null);
        if(p!=null){
            return p.getStockMvms().stream().map(mapper1::toDTO).collect(Collectors.toList());
        }
        return null;
    }
    
    @CacheEvict(value={"ProductById", "ProductByName", "AllProducts"}, allEntries=true)
    public void clearCache(){
        
    }

}
