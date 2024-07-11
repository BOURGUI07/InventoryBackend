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
import java.util.stream.Collectors;
import main.dto.BestCustomerDTO;
import main.dto.CompanyProductsDTO;
import main.dto.CustomersNoOrdersDTO;
import main.dto.PopularProductDTO;
import main.dto.ProductDTO;
import main.dto.ProductHighestQtyDTO;
import main.dto.ProductQtyDTO;
import main.dto.SalesByCategoryDTO;
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
    
    
    
    //Retrieve the top 5 products with the highest total sales quantity.
    public List<ProductHighestQtyDTO> productsWithHighestQty(){
        var q = "SELECT p.product_id, p.product_name, SUM(d.quantity) AS total_sales_qty "
                + "FROM product p JOIN sales_detail d "
                + "ON p.product_id = d.product_id "
                + "GROUP BY p.product_id, p.product_name "
                + "ORDER BY total_sales_qty DESC "
                + "LIMIT 5";
        List<Object[]> result = em.createNativeQuery(q).getResultList();
        return result.stream().map(x -> new ProductHighestQtyDTO((Integer) x[0], (String) x[1], (int) x[2])).collect(Collectors.toList());
    }
    
    //Calculate the total sales amount for each category.
    public List<SalesByCategoryDTO> salesByCategory(){
        var q = "SELECT c.category_id, c.category_name, SUM(p.price*SUM(d.quantity)) AS total_sales"
                + "FROM product p JOIN sales_detail d "
                + "ON p.product_id = d.product_id "
                + "JOIN category c ON p.category_id = c.category_id "
                + "GROUP BY c.category_id, c.category_name "
                + "ORDER BY total_sales DESC ";
        List<Object[]> result = em.createNativeQuery(q).getResultList();
        return result.stream().map(x -> new SalesByCategoryDTO((Integer) x[0], (String) x[1], (BigDecimal) x[2])).collect(Collectors.toList());
    }
    
    //Retrieve customers who have not placed any orders.
    public List<CustomersNoOrdersDTO> customersWithNoOrders(){
        var q = "SELECT c.customer_id, CONCAT(c.firstname, ' ' , c.lastname) AS fullname "
                + "FROM customer c LEFT JOIN cust_order o "
                + "ON c.customer_id = o.customer_id "
                + "WHERE o.cust_order_id IS NULL";
        List<Object[]> result = em.createNativeQuery(q).getResultList();
        return result.stream().map(x -> new CustomersNoOrdersDTO((Integer)x[0],(String) x[1])).collect(Collectors.toList());
    }
    
    //Retrieve products where the stock quantity is below a specified threshold.
    public List<ProductQtyDTO> productsQtyBelow(Integer qty){
        var q = "SELECT p.product_id, p.product_name, s.quantity "
                + "FROM product p JOIN stock_mvm s "
                + "ON p.product_id = s.product_id "
                + "WHERE s.quantity<= :x ";
        List<Object[]> result = em.createNativeQuery(q).getResultList();
        return result.stream().map(x -> new ProductQtyDTO((Integer)x[0],(String) x[1], (Integer) x[2])).collect(Collectors.toList()); 
    }
    
    //List all products belonging to a specific company, including their categories and prices.
    public List<CompanyProductsDTO> productsBelongingToCompany(String companyName){
        var q = "SELECT p.product_id, p.product_name, p.price, c.category_name "
                + "FROM product p JOIN category c "
                + "ON p.category_id = c.category_id "
                + "JOIN company m ON p.company_id = m.company_id "
                + "WHERE m.company_name= :x";
        List<Object[]> result = em.createNativeQuery(q).setParameter("x", companyName).getResultList();
        return result.stream().map(x -> new CompanyProductsDTO((Integer) x[0], (String) x[1], (BigDecimal) x[2], (String) x[3] )).collect(Collectors.toList());
    }
    
    
    
    /*
Get the top-selling products for each company based on total sales quantity.

List all orders placed by a specific customer, including order details.

Retrieve a hierarchical view of a company's structure, including user roles and responsibilities.

Identify products that have the most associated sales details.

Calculate the average order value for each customer.

Get historical sales data for a product, including sales quantity and total sales amount.

Retrieve products that have had stock movements within the last month.

List orders along with the suppliers who provided the products for those orders.

Identify categories that do not have any associated products.
    */
  
}
