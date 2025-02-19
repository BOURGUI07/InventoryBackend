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
import main.dto.CategoryDTO;
import main.entity.Category;
import main.mapper.CategoryMapper;
import main.repo.CategRepo;
import main.repo.ProductRepo;
import main.specification.CategorySpecification;
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
public class CategoryService {

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public CategoryService(CategoryMapper mapper, CategRepo repo, ProductRepo Productrepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.productRepo = Productrepo;
    }
    @PersistenceContext
    private EntityManager em;
    private final CategoryMapper mapper;
    private final CategRepo repo;
    private final ProductRepo productRepo;
    private Validator validator;
    
    @Transactional
    @CacheEvict(value={"CategoryById","AllCategories","CategoryByName"}, allEntries=true)
    public CategoryDTO create(CategoryDTO x){
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var c = mapper.toEntity(x);
        var saved = repo.save(c);
        return mapper.toDTO(saved);
    }
    
    @Transactional
    @CacheEvict(value={"CategoryById","AllCategories","CategoryByName"}, allEntries=true)
    public CategoryDTO update(Integer id, CategoryDTO x){
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(x);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            c.setDesc(x.desc());
            c.setName(x.name());
            if(x.productIds()!=null){
                c.setProducts(productRepo.findAllById(x.productIds()));
            }
            var saved = repo.save(c);
            return mapper.toDTO(saved);
        }
        return null;
    }
    
    @Transactional
    @CacheEvict(value={"CategoryById","AllCategories","CategoryByName"}, allEntries=true)
    public void delete(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            var list = c.getProducts();
            if(!list.isEmpty()){
                list.stream().forEach(c::removeProduct);
                productRepo.saveAll(list);
            }
            repo.delete(c);
        }
    }
    
    @Transactional
    @CacheEvict(value={"CategoryById","AllCategories","CategoryByName"}, allEntries=true)
    public void addProductToCateg(Integer categId, Integer productId){
        var c = repo.findById(categId).orElse(null);
        if(c!=null){
            var p = productRepo.findById(productId).orElse(null);
            if(p!=null){
                c.addProduct(p);
                repo.save(c);
                productRepo.save(p);
            }
        }
    }  
    
    
    @Transactional
    @CacheEvict(value={"CategoryById","AllCategories","CategoryByName"}, allEntries=true)
    public void removeProductFromCateg(Integer categId, Integer productId){
        var c = repo.findById(categId).orElse(null);
        if(c!=null){
            var p = productRepo.findById(productId).orElse(null);
            if(p!=null){
                c.removeProduct(p);
                repo.save(c);
                productRepo.save(p);
            }
        }
    }
    
    @Cacheable(value="CategoryById", key="#id")
    public CategoryDTO findById(Integer id){
        var c = repo.findById(id).orElse(null);
        if(c!=null){
            return mapper.toDTO(c);
        }
        return null;
    }
    
    @Cacheable(value="AllCategories", key="#root.methodName")
    public List<CategoryDTO> findAll(){
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }
    
    @Cacheable(value="CategoryByName", key="#name")
    public CategoryDTO findByName(String name){
        var q = "SELECT * FROM category WHERE category_name= :x";
        Category c =  (Category)em.createNativeQuery(q, Category.class).setHint("x", name).getSingleResult();
        try{
            return mapper.toDTO(c);
        }catch(NoResultException e){
            return null;
        }catch(NonUniqueResultException e){
            return null;
        }
    }
    
    @CacheEvict(value={"CategoryById","AllCategories","CategoryByName"}, allEntries=true)
    public void clearCache(){
    }
    
    public Page<CategoryDTO> findAllPaginated(Pageable pageable, String name, String desc){
        Specification<Category> spec = Specification.where(null);
        if(name!=null && !name.isEmpty()){
            spec = spec.and(CategorySpecification.nameContains(name));
        }
        if(desc!=null && !desc.isEmpty()){
            spec = spec.and(CategorySpecification.descContains(desc));
        }
        Page<Category> page = repo.findAll(spec, pageable);
        return page.map(mapper::toDTO);
    }
}
