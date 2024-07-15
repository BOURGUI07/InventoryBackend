package Inventory.Inv;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import main.controller.SupplierController;
import main.dto.SupplierDTO;
import main.entity.Address;
import main.service.SupplierService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author hp
 */
@AutoConfigureMockMvc
public class SupplierControllerTest {

    @Mock
    private SupplierService service;

    @InjectMocks
    private SupplierController controller;
    
    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();
    private SupplierDTO x = new SupplierDTO(1,"first","last",new Address(),"younessbourgui07@gmail.com","0606","pic", new ArrayList<>());

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testFindAll() throws Exception {
        var list = Collections.singletonList(x);
        when(service.findAll()).thenReturn(list);
        mvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(x.id()))
                .andExpect(jsonPath("$[0].firstName").value(x.firstName()))
                .andExpect(jsonPath("$[0].lastName").value(x.lastName()))
                .andExpect(jsonPath("$[0].address").value(x.address()))
                .andExpect(jsonPath("$[0].email").value(x.email()))
                .andExpect(jsonPath("$[0].phone").value(x.phone()))
                .andExpect(jsonPath("$[0].pic").value(x.pic()))
                .andExpect(jsonPath("$[0].suppOrderIds").isArray());
    }

    @Test
    void testFindAllIsEmpty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/suppliers")).andExpect(status().isNoContent());
    }
    
    @Test
    void testFindById() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.firstName").value(x.firstName()))
                .andExpect(jsonPath("$.lastName").value(x.lastName()))
                .andExpect(jsonPath("$.address").value(x.address()))
                .andExpect(jsonPath("$.email").value(x.email()))
                .andExpect(jsonPath("$.phone").value(x.phone()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.suppOrderIds").isArray());
    }
    
    @Test
    void testFindById_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testFindByName() throws Exception{
        when(service.findByFirstOrLastName("first","last")).thenReturn(x);
        mvc.perform(get("/api/suppliers/search?firstname=first&lastname=last"))
                .andDo(print()) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.firstName").value(x.firstName()))
                .andExpect(jsonPath("$.lastName").value(x.lastName()))
                .andExpect(jsonPath("$.address").value(x.address()))
                .andExpect(jsonPath("$.email").value(x.email()))
                .andExpect(jsonPath("$.phone").value(x.phone()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.suppOrderIds").isArray());
    }
    
    @Test
    void testFindByBlankName() throws Exception{
        when(service.findByFirstOrLastName("first","last")).thenReturn(null);
        mvc.perform(get("/api/suppliers/name/"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCreate() throws Exception {
        when(service.create(any(SupplierDTO.class))).thenReturn(x);

        mvc.perform(post("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.firstName").value(x.firstName()))
                .andExpect(jsonPath("$.lastName").value(x.lastName()))
                .andExpect(jsonPath("$.address").value(x.address()))
                .andExpect(jsonPath("$.email").value(x.email()))
                .andExpect(jsonPath("$.phone").value(x.phone()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.suppOrderIds").isArray());
    }
    
    @Test
    void testUpdate() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.update(anyInt(), any(SupplierDTO.class))).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/suppliers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.firstName").value(x.firstName()))
                .andExpect(jsonPath("$.lastName").value(x.lastName()))
                .andExpect(jsonPath("$.address").value(x.address()))
                .andExpect(jsonPath("$.email").value(x.email()))
                .andExpect(jsonPath("$.phone").value(x.phone()))
                .andExpect(jsonPath("$.pic").value(x.pic()))
                .andExpect(jsonPath("$.suppOrderIds").isArray());
    }
    
    @Test
    void testUpdate_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/suppliers/1")
           .contentType(MediaType.APPLICATION_JSON)
           .content(mapper.writeValueAsString(x)))
           .andExpect(status().isNotFound());
    }
    
    @Test
    void testDelete() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(delete("/api/suppliers/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testDeleteNotFound() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(delete("/api/suppliers/1"))
           .andExpect(status().isNotFound());
    }
    
    @Test
    void testAddOrder() throws Exception{
        doNothing().when(service).addOrder(1, 1);
        mvc.perform(put("/api/suppliers/1/orders/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testRemoveOrder() throws Exception{
        doNothing().when(service).removeOrder(1, 1);
        mvc.perform(delete("/api/suppliers/1/orders/1"))
                .andExpect(status().isNoContent());
    }
}
