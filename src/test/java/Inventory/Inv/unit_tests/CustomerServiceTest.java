/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Inventory.Inv.unit_tests;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import main.dto.CustOrderDTO;
import main.dto.CustomerDTO;
import main.entity.Address;
import main.entity.CustOrder;
import main.entity.Customer;
import main.mapper.CustomerMapper;
import main.repo.CustOrderRepo;
import main.repo.CustomerRepo;
import main.service.CustomerService;
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
public class CustomerServiceTest {
    
    @Mock
    private CustomerMapper mapper;
    
    @Mock
    private CustomerRepo repo;
    
    @Mock
    private CustOrderRepo orderRepo;
    
    @InjectMocks
    private CustomerService service;
    
    private Validator validator;
    
    private Customer e = new Customer(1,"first","last",new Address(),"younessbourgui07@gmail.com","0606","pic", new ArrayList<>());
    private CustomerDTO x = new CustomerDTO(1,"first","last",new Address(),"younessbourgui07@gmail.com","0606","pic", new ArrayList<>());
    
    private CustOrder e1 = new CustOrder(1,"code", Instant.now(),null, new ArrayList<>());
    
    public CustomerServiceTest() {
        
        
    }
    
    
    
    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        service.setValidator(validator);
    }
    
    @Test
    void testCreate(){
        when(mapper.toEntity(x)).thenReturn(e);
        when(repo.save(e)).thenReturn(e);
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(x,service.create(x));
        
        verify(repo,times(1)).save(e);                                               
    }
    
    
    @Test
    void testUpdate(){
        when(repo.findById(1)).thenReturn(Optional.of(e));
        when(repo.save(e)).thenReturn(e);
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(x,service.update(1,x));
        
        verify(repo,times(1)).save(e);                                               
    }
    
    @Test
    void testFindById(){
        when(repo.findById(1)).thenReturn(Optional.of(e));
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(x,service.findById(1));
    }
    
    @Test
    void testFindAll(){
        when(repo.findAll()).thenReturn(Arrays.asList(e));
        when(mapper.toDTO(e)).thenReturn(x);
        
        assertEquals(1,service.findAll().size());
    }
    
    @Test
    void testDeleet(){
        doNothing().when(repo).delete(e);
        when(repo.findById(1)).thenReturn(Optional.of(e));
        service.delete(1);
        verify(repo,times(1)).delete(e);
    }
    
    @Test
    void testAddRemoveOrder(){
        when(repo.findById(1)).thenReturn(Optional.of(e));
        when(orderRepo.findById(1)).thenReturn(Optional.of(e1));
        service.addOrder(1, 1);
        assertTrue(e1.getCustomer().equals(e));
        service.removeOrder(1, 1);
        assertTrue(e1.getCustomer()==null);
    }
    
    @Test
    void testEmailValidation(){
        var s = new CustomerDTO(1,"first","last",new Address(),"email","0606","pic", new ArrayList<>());
        assertThrows(ConstraintViolationException.class, () -> {
            service.create(s);
        });
    }
}
