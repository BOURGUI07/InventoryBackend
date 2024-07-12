/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Inventory.Inv.unit_tests;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import main.dto.CompanyDTO;
import main.entity.Company;
import main.mapper.CompanyMapper;
import main.repo.CompanyRepo;
import main.repo.ProductRepo;
import main.repo.UserRepo;
import main.service.CompanyService;
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
public class CompanyServiceTest {
    
    @Mock
    private CompanyMapper mapper;
    
    @Mock
    private CompanyRepo repo;
    
    @Mock
    private ProductRepo productRepo;
    
    @Mock
    private UserRepo userRepo;
    
    private Validator validator;
    
    @InjectMocks
    private CompanyService service;
    
    private CompanyDTO x;
    private Company e = new Company();
    public CompanyServiceTest() {
        x= new CompanyDTO(1,"name",new ArrayList<>(),new ArrayList<>() );
        e.setId(1);
        e.setUsers(new ArrayList<>());
        e.setProducts(new ArrayList<>());
        e.setName("name");
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
       var c1 = new CompanyDTO(1,"",new ArrayList<>() , new ArrayList<>());
       assertThrows(ConstraintViolationException.class, () -> {
            service.create(c1);
        });
   }
}
