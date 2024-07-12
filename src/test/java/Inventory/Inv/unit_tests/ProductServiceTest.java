/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Inventory.Inv.unit_tests;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import main.dto.CompanyDTO;
import main.dto.ProductDTO;
import main.entity.Company;
import main.entity.CustOrderDetailId;
import main.entity.Product;
import main.entity.SalesDetailId;
import main.entity.SuppOrderDetailId;
import main.mapper.CompanyMapper;
import main.mapper.ProductMapper;
import main.mapper.StockMvmMapper;
import main.repo.CategRepo;
import main.repo.CompanyRepo;
import main.repo.CustOrderDetailRepo;
import main.repo.ProductRepo;
import main.repo.SalesDetailRepo;
import main.repo.StockMvmRepo;
import main.repo.UserRepo;
import main.service.CompanyService;
import main.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author hp
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    
    @Mock
    private ProductMapper mapper;
    
    @Mock
    private ProductRepo repo;
    
    @Mock
    private CategRepo categoryRepo;
    
    @Mock
    private ProductRepo productRepo;
    
    @Mock
    private UserRepo userRepo;
    
    @Mock
    private CompanyRepo companyRepo;
    
    @Mock
    private CustOrderDetailRepo custRepo;
    
    @Mock
    private StockMvmRepo stockRepo;
    
    @Mock
    private SalesDetailRepo salesRepo;
    
    @Mock
    private StockMvmMapper mapper1;
    
    private Validator validator;
    
    @InjectMocks
    private ProductService service;
    
    private ProductDTO x;
    private Product e = new Product();
    public ProductServiceTest() {
        x = new ProductDTO(1,"name", "desrip", new BigDecimal("123.540"), "", 1, 1, new ArrayList<Integer>(),new ArrayList<SalesDetailId>(),new ArrayList<CustOrderDetailId>(),new ArrayList<SuppOrderDetailId>());
        e.setId(1);
        e.setCustOrderDetails(new ArrayList<>());
        e.setSuppOrderDetails(new ArrayList<>());
        e.setStockMvms(new ArrayList<>());
        e.setSalesDetails(new ArrayList<>());
        e.setDesc("desrip");
        e.setName("name");
        e.setPic("");
        e.setCompany(null);
        e.setPrice(new BigDecimal("123.540"));
        e.setCateg(null);
    }
       
    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        service.setValidator(validator);
    }
    
    @Test
    void testCreateEntity(){
        when(mapper.toEntity(x)).thenReturn(e);
        when(repo.save(e)).thenReturn(e);
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(x,service.create(x));
        
        verify(repo, times(1)).save(e);
    }
    
    @Test
    void testUpdateEntity(){
        when(repo.findById(1)).thenReturn(Optional.of(e));
        when(repo.save(e)).thenReturn(e);
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(x,service.update(1, x));
        
        verify(repo,times(1)).save(e);
    }
    
    @Test
    void testFindById(){
        when(repo.findById(1)).thenReturn(Optional.of(e));
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(x, service.findById(1));
    }
    
    @Test
    void testFindAll(){
        when(repo.findAll()).thenReturn(Arrays.asList(e));
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(1, service.findAll().size());
    }
    
    @Test
    void testDeleteEntity(){
        doNothing().when(repo).delete(e);
        when(repo.findById(1)).thenReturn(Optional.of(e));
        service.delete(1);
        verify(repo,times(1)).delete(e);
    }
    
    @Test
   void testValidation(){
       var c1 = new ProductDTO(1,"", "desrip", new BigDecimal("123.540"), "", 1, 1, new ArrayList<Integer>(),new ArrayList<SalesDetailId>(),new ArrayList<CustOrderDetailId>(),new ArrayList<SuppOrderDetailId>());
       assertThrows(ConstraintViolationException.class, () -> {
            service.create(c1);
        });
   }
}
