/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author hp
 */
@Entity
@Table(name="cust_order_detail")
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class CustOrderDetail extends BaseEntity{
    @EmbeddedId
    private CustOrderDetailId id;
    
    @ManyToOne
    @JoinColumn(name="product_id")
    @JsonBackReference
    private Product product;
    
    @ManyToOne
    @JoinColumn(name= "cust_order_id")
    @JsonBackReference
    private CustOrder custOrder;
    
    @Column(name="quantity")
    private Integer quantity;
}
