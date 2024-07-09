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
@Table(name="supp_order")
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class SuppOrder extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="supp_order_id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name="supplier_id")
    @JsonBackReference
    private Supplier supplier;
    
    @OneToMany(mappedBy="suppOrder")
    @JsonManagedReference
    private List<SuppOrderDetail> suppOrderDetails  = new ArrayList<>();
    
    @Column(name="order_code")
    private String code;
    
    
    @Column(name="order_date")
    private Instant orderDate;
    
    public void addDetail(SuppOrderDetail d){
        this.suppOrderDetails.add(d);
        d.setSuppOrder(this);
    }
    
    public void removeDetail(SuppOrderDetail d){
        this.suppOrderDetails.remove(d);
        d.setSuppOrder(null);
    }
}
