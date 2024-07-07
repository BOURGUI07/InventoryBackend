/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.SupplierDTO;
import main.entity.Supplier;
import main.repo.SuppOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SupplierMapper {
    @Autowired
    public SupplierMapper(SuppOrderRepo repo) {
        this.repo = repo;
    }
    private SuppOrderRepo repo;
    
    public Supplier toEntity(SupplierDTO x){
        var c = new Supplier();
        c.setAddress(x.address());
        c.setEmail(x.email());
        c.setPic(x.pic());
        c.setPhone(x.phone());
        c.setFirstName(x.firstName());
        c.setLastName(x.lastName());
        if(x.suppOrderIds()!=null){
            c.setSuppOrders(repo.findAllById(x.suppOrderIds()));
        }
        return c;
    }
    
    public SupplierDTO toDTO(Supplier c){
        var list = c.getSuppOrders().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new SupplierDTO(c.getId(),c.getFirstName(),c.getLastName(),c.getAddress(),c.getEmail(),c.getPhone(),c.getPic(),list);
    }
}
