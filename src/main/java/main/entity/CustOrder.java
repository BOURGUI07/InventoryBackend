/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
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
@Table(name="cust_order")
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class CustOrder extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="cust_order_id")
    private Integer id;
    
    @Column(name="order_code")
    private String code;
    
    
    @Column(name="order_date")
    private Instant orderDate;
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonBackReference
    private Customer customer;
    
    @OneToMany(mappedBy="custOrder")
    @JsonManagedReference
    private List<CustOrderDetail> custOrderDetails = new ArrayList<>();
    
    
    public void addDetail(CustOrderDetail d){
        this.custOrderDetails.add(d);
        d.setCustOrder(this);
    }
    
    public void removeDetail(CustOrderDetail d){
        this.custOrderDetails.remove(d);
        d.setCustOrder(null);
    }
}
