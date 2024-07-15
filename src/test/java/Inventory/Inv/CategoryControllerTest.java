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
import main.dto.CategoryDTO;
import main.service.CategoryService;
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
public class CategoryControllerTest {

    @Mock
    private CategoryService service;

    @InjectMocks
    private CategoryController controller;
    
    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();
    private CategoryDTO x = new CategoryDTO(1, "electronics", "all electronic categories", new ArrayList<>());

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testFindAll() throws Exception {
        var list = Collections.singletonList(x);
        when(service.findAll()).thenReturn(list);
        mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(x.id()))
                .andExpect(jsonPath("$[0].name").value(x.name()))
                .andExpect(jsonPath("$[0].desc").value(x.desc()))
                .andExpect(jsonPath("$[0].productIds").isArray());
    }

    @Test
    void testFindAllIsEmpty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get("/api/categories")).andExpect(status().isNoContent());
    }
    
    @Test
    void testFindById() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.desc").value(x.desc()))
                .andExpect(jsonPath("$.productIds").isArray());
    }
    
    @Test
    void testFindById_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testFindByName() throws Exception{
        when(service.findByName("electronics")).thenReturn(x);
        mvc.perform(get("/api/categories/name/electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(x.id()))
                .andExpect(jsonPath("$.name").value(x.name()))
                .andExpect(jsonPath("$.desc").value(x.desc()))
                .andExpect(jsonPath("$.productIds").isArray());
    }
    
    @Test
    void testFindByBlankName() throws Exception{
        when(service.findByName("")).thenReturn(null);
        mvc.perform(get("/api/categories/name/"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCreate() throws Exception {
        when(service.create(any(CategoryDTO.class))).thenReturn(x);

        mvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(x.id()))
            .andExpect(jsonPath("$.name").value(x.name()))
            .andExpect(jsonPath("$.desc").value(x.desc()))
            .andExpect(jsonPath("$.productIds").isArray());
    }
    
    @Test
    void testUpdate() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.update(anyInt(), any(CategoryDTO.class))).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/categories/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(x)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(x.id()))
            .andExpect(jsonPath("$.name").value(x.name()))
            .andExpect(jsonPath("$.desc").value(x.desc()))
            .andExpect(jsonPath("$.productIds").isArray());
    }
    
    @Test
    void testUpdate_not_found() throws Exception{
        when(service.findById(1)).thenReturn(null);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(put("/api/categories/1")
           .contentType(MediaType.APPLICATION_JSON)
           .content(mapper.writeValueAsString(x)))
           .andExpect(status().isNotFound());
    }
    
    @Test
    void testDelete() throws Exception{
        when(service.findById(1)).thenReturn(x);
        when(service.findAll()).thenReturn(Collections.singletonList(x));
        mvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}
