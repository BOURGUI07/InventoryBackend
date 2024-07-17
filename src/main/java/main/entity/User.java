/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name="users")
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer id;
     
    @Column(name="username")
    private String username;
    
    @Column(name="password")
    private String password;
    
    @Column(name="email")
    private String email;
    
    private boolean enabled;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="company_id")
    @JsonBackReference
    private Company company;
    
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="user_roles", 
                joinColumns =@JoinColumn(name="user_id"), 
                inverseJoinColumns=@JoinColumn(name="role_id"))
    private List<Role> roles = new ArrayList<>();
    
    
}
