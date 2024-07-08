/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name="company")
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="company_id")
    private Integer id;
    
    @OneToMany(mappedBy="company")
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
    
    @OneToMany(mappedBy="company")
    @JsonManagedReference
    private List<User> users = new ArrayList<>();
    
    @Column(name="company_name")
    private String name;
    
    public void addProduct(Product p){
        products.add(p);
        p.setCompany(this);
    }
    
    public void removeProduct(Product p){
        products.remove(p);
        p.setCompany(null);
    }
    
    public void addUser(User p){
        users.add(p);
        p.setCompany(this);
    }
    
    public void removeUser(User p){
        users.remove(p);
        p.setCompany(null);
    }
}
