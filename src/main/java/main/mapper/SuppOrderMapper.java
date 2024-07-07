/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.SuppOrderDTO;
import main.entity.SuppOrder;
import main.repo.SuppOrderDetailRepo;
import main.repo.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SuppOrderMapper {
    @Autowired
    public SuppOrderMapper(SupplierRepo repo, SuppOrderDetailRepo repo1) {
        this.repo = repo;
        this.repo1 = repo1;
    }
    private SupplierRepo repo;
    private SuppOrderDetailRepo repo1;
    
    public SuppOrder toEntity(SuppOrderDTO x){
        var s = new SuppOrder();
        s.setCode(x.code());
        s.setSupplier(repo.findById(x.supplierId()).orElse(null));
        s.setOrderDate(x.date());
        if(x.suppOrderDetailIds()!=null){
            s.setSuppOrderDetails(repo1.findAllById(x.suppOrderDetailIds()));
        }
        return s;
    }
    
    public SuppOrderDTO toDTO(SuppOrder s){
        var list = s.getSuppOrderDetails().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new SuppOrderDTO(s.getId(),s.getCode(),s.getOrderDate(),s.getSupplier().getId() ,list);
    }
}
