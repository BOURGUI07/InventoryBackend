/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import main.dto.BestCustomerDTO;
import main.dto.CompanyDTO;
import main.dto.PopularProductDTO;
import main.dto.ProductDTO;
import main.dto.SalesReportDTO;
import main.dto.StockMvmDTO;
import main.mapper.ProductMapper;
import main.mapper.StockMvmMapper;
import main.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class GlobalService {
    @Autowired
    public GlobalService(ProductRepo productRepo, ProductMapper mapper, StockMvmMapper mapper1) {
        this.productRepo = productRepo;
        this.mapper = mapper;
        this.mapper1 = mapper1;
    }
    private final ProductRepo productRepo;
    private final ProductMapper mapper;
    private final StockMvmMapper mapper1;
    @PersistenceContext
    private EntityManager em;
    
    
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
    
    //Implement a method to provide statistics on the most popular products, best customers, etc.
    public List<PopularProductDTO> getMostPopularProducts(Instant startDate, Instant endDate){
        var q = "SELECT p.product_id, p.product_name, COUNT(d.cust_order_id) "
                + "FROM cust_order o "
                + "JOIN cust_order_detail d ON o.cust_order_id = d.cust_order_id "
                + "JOIN product p ON d.product_id = p.product_id "
                + "WHERE o.order_date BETWEEN :x AND :y "
                + "GROUP BY p.product_id, p.product_name "
                + "ORDER BY COUNT(d.cust_order_id) DESC";
        List<Object[]> result =  em.createNativeQuery(q).setParameter("x",startDate).setParameter("y", endDate).getResultList();
        return result.stream().map(x -> new PopularProductDTO((Integer)x[0], (String) x[1], (int) x[2])).collect(Collectors.toList());
    }
    public List<BestCustomerDTO> getBestCustomers(Instant startDate, Instant endDate){
        var q = "SELECT c.customer_id, CONCAT(c.firstname, ' ' , c.lastname) AS fullname, COUNT(o.cust_order_id) "
                + "FROM customer c JOIN cust_order o "
                + "ON c.customer_id = o.customer_id "
                + "WHERE o.order_date BETWEEN :x AND :y "
                + "GROUP BY c.customer_id, c.firstname, c.lastname "
                + "ORDER BY COUNT(o.cust_order_id) DESC";
        List<Object[]> result = em.createNativeQuery(q).setParameter("x", startDate).setParameter("y", endDate).getResultList();
        return result.stream().map(x -> new BestCustomerDTO((Integer)x[0],(String) x[1],(int) x[2])).collect(Collectors.toList());
    }
  
}
