/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author hp
 */
@Entity
@Table(name="product")
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="product_id")
    private Integer id;
    
    
    @Column(name="product_name")
    private String name;
    
    @Column(name="product_desc")
    private String desc;
    
    @Column(name="price")
    private BigDecimal price;
    
    @Column(name="vat_rate")
    private BigDecimal vatRate;
    
    @Column(name="price_ttc")
    private BigDecimal priceTTC;
    
    @Column(name="pic")
    private String pic;
    
    
    @OneToMany(mappedBy="product")
    @JsonManagedReference
    private List<StockMvm> stockMvms = new ArrayList<>();
    
    
    @OneToMany(mappedBy="product")
    @JsonManagedReference
    private List<CustOrderDetail> custOrderDetails = new ArrayList<>();
    
    
    @OneToMany(mappedBy="product")
    @JsonManagedReference
    private List<SuppOrderDetail> suppOrderDetails = new ArrayList<>();
    
    
    
    @OneToMany(mappedBy="product")
    @JsonManagedReference
    private List<SalesDetail> salesDetails = new ArrayList<>();
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    @JsonBackReference
    private Category categ;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id")
    @JsonBackReference
    private Company company;
}
