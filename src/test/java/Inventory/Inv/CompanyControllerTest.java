/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Inventory.Inv;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import main.controller.CategoryController;
import main.controller.CompanyController;
import main.dto.CategoryDTO;
import main.dto.CompanyDTO;
import main.service.CategoryService;
import main.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author hp
 */
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Mock
    private CompanyService service;

    @InjectMocks
    private CompanyController controller;
    
    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();
    private CompanyDTO x = new CompanyDTO(1, "name", new ArrayList<>(), new ArrayList<>());

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testFindAll() throws Exception {
        var list = Collections.singletonList(x);
        when(service.findAll()).thenReturn(list);
        mvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(x.id()))
                .andExpect(jsonPath("$[0].name").value(x.name()))
                .andExpect(jsonPath("$[0].productIds").isArray())
                .andExpect(jsonPath("$[0].userIds").isArray());
    }

    @Test
    void testFindAllIsEmpty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/companies")).andExpect(status().isNoContent());
    }
    
    @Test
    void testFindById() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.productIds").isArray())
                .andExpect(jsonPath("$.userIds").isArray());
    }
    
    @Test
    void testFindById_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/companies/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testFindByName() throws Exception{
        when(service.findByName("name")).thenReturn(x);
        mvc.perform(get("/api/companies/name/name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.productIds").isArray())
                .andExpect(jsonPath("$.userIds").isArray());
    }
    
    @Test
    void testFindByBlankName() throws Exception{
        when(service.findByName("")).thenReturn(null);
        mvc.perform(get("/api/companies/name/"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCreate() throws Exception {
        when(service.create(any(CompanyDTO.class))).thenReturn(x);

        mvc.perform(post("/api/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.productIds").isArray())
                .andExpect(jsonPath("$.userIds").isArray());
    }
    
    @Test
    void testUpdate() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.update(anyInt(), any(CompanyDTO.class))).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/companies/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.productIds").isArray())
                .andExpect(jsonPath("$.userIds").isArray());
    }
    
    @Test
    void testUpdate_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/companies/1")
           .contentType(MediaType.APPLICATION_JSON)
           .content(mapper.writeValueAsString(x)))
           .andExpect(status().isNotFound());
    }
    
    @Test
    void testDelete() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(delete("/api/companies/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testDeleteNotFound() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(delete("/api/companies/1"))
           .andExpect(status().isNotFound());
    }
    
    @Test
    void testAddProduct() throws Exception{
        doNothing().when(service).addProductToCompany(1, 1);
        mvc.perform(put("/api/companies/1/products/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testRemoveProduct() throws Exception{
        doNothing().when(service).removeProductFromCompany(1, 1);
        mvc.perform(delete("/api/companies/1/products/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testAddUser() throws Exception{
        doNothing().when(service).addUserToCompany(1, 1);
        mvc.perform(put("/api/companies/1/users/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testRemoveUser() throws Exception{
        doNothing().when(service).removeUserFromCompany(1, 1);
        mvc.perform(delete("/api/companies/1/users/1"))
                .andExpect(status().isNoContent());
    }
}
