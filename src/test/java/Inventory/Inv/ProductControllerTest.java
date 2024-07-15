/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Inventory.Inv;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import main.controller.ProductController;
import main.dto.ProductDTO;
import main.entity.CustOrderDetailId;
import main.entity.SalesDetailId;
import main.entity.SuppOrderDetailId;
import main.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
public class ProductControllerTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;
    
    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();
    private ProductDTO x = new ProductDTO(1,"name", "desc", new BigDecimal("123.54"), "", 1, 1, new ArrayList<Integer>(),new ArrayList<SalesDetailId>(),new ArrayList<CustOrderDetailId>(),new ArrayList<SuppOrderDetailId>());

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testFindAll() throws Exception {
        var list = Collections.singletonList(x);
        when(service.findAll()).thenReturn(list);
        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(x.id()))
                .andExpect(jsonPath("$[0].name").value(x.name()))
                .andExpect(jsonPath("$[0].desc").value(x.desc()))
                .andExpect(jsonPath("$[0].price").value(x.price()))
                .andExpect(jsonPath("$[0].pic").value(x.pic()))
                .andExpect(jsonPath("$[0].categoryId").value(x.categoryId()))
                .andExpect(jsonPath("$[0].companyId").value(x.companyId()))
                .andExpect(jsonPath("$[0].stockMvmIds").isArray())
                .andExpect(jsonPath("$[0].salesDetailIds").isArray())
                .andExpect(jsonPath("$[0].custOrderDetailIds").isArray())
                .andExpect(jsonPath("$[0].suppOrderDetailIds").isArray());
    }

    @Test
    void testFindAllIsEmpty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/products")).andExpect(status().isNoContent());
    }
    
    @Test
    void testFindById() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(11))
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.desc").value(x.desc()))
                .andExpect(jsonPath("$.price").value(x.price()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.categoryId").value(x.categoryId()))
                .andExpect(jsonPath("$.companyId").value(x.companyId()))
                .andExpect(jsonPath("$.stockMvmIds").isArray())
                .andExpect(jsonPath("$.salesDetailIds").isArray())
                .andExpect(jsonPath("$.custOrderDetailIds").isArray())
                .andExpect(jsonPath("$.suppOrderDetailIds").isArray());
    }
    
    @Test
    void testFindById_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testFindByName() throws Exception{
        when(service.findByName("name")).thenReturn(x);
        
        mvc.perform(get("/api/products/name/name"))
                .andExpect(jsonPath("$.length()").value(11))
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.desc").value(x.desc()))
                .andExpect(jsonPath("$.price").value(x.price()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.categoryId").value(x.categoryId()))
                .andExpect(jsonPath("$.companyId").value(x.companyId()))
                .andExpect(jsonPath("$.stockMvmIds").isArray())
                .andExpect(jsonPath("$.salesDetailIds").isArray())
                .andExpect(jsonPath("$.custOrderDetailIds").isArray())
                .andExpect(jsonPath("$.suppOrderDetailIds").isArray());
    }
    
    @Test
    void testFindByBlankName() throws Exception{
        when(service.findByName("")).thenReturn(null);
        mvc.perform(get("/api/products/name/"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCreate() throws Exception {
        when(service.create(any(ProductDTO.class))).thenReturn(x);

        mvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.length()").value(11))
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.desc").value(x.desc()))
                .andExpect(jsonPath("$.price").value(x.price()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.categoryId").value(x.categoryId()))
                .andExpect(jsonPath("$.companyId").value(x.companyId()))
                .andExpect(jsonPath("$.stockMvmIds").isArray())
                .andExpect(jsonPath("$.salesDetailIds").isArray())
                .andExpect(jsonPath("$.custOrderDetailIds").isArray())
                .andExpect(jsonPath("$.suppOrderDetailIds").isArray());
    }
    
    @Test
    void testUpdate() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.update(anyInt(), any(ProductDTO.class))).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(11))
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.desc").value(x.desc()))
                .andExpect(jsonPath("$.price").value(x.price()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.categoryId").value(x.categoryId()))
                .andExpect(jsonPath("$.companyId").value(x.companyId()))
                .andExpect(jsonPath("$.stockMvmIds").isArray())
                .andExpect(jsonPath("$.salesDetailIds").isArray())
                .andExpect(jsonPath("$.custOrderDetailIds").isArray())
                .andExpect(jsonPath("$.suppOrderDetailIds").isArray());
    }
    
    @Test
    void testUpdate_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/products/1")
           .contentType(MediaType.APPLICATION_JSON)
           .content(mapper.writeValueAsString(x)))
           .andExpect(status().isNotFound());
    }
    
    @Test
    void testDelete() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testDeleteNotFound() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(delete("/api/products/1"))
           .andExpect(status().isNotFound());
    }
}
