/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import main.dto.SuppOrderDetailDTO;
import main.entity.SuppOrderDetail;
import main.repo.ProductRepo;
import main.repo.SuppOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SuppOrderDetailMapper {
    @Autowired
    public SuppOrderDetailMapper(ProductRepo repo, SuppOrderRepo repo1) {
        this.repo = repo;
        this.repo1 = repo1;
    }
    private ProductRepo repo;
    private SuppOrderRepo repo1;
    
    public SuppOrderDetail toEntity(SuppOrderDetailDTO x){
        var s = new SuppOrderDetail();
        s.setQuantity(x.quantity());
        s.setProduct(repo.findById(x.productId()).orElse(null));
        s.setSuppOrder(repo1.findById(x.suppOrderId()).orElse(null));
        return s;
    }
    
    public SuppOrderDetailDTO toDTO(SuppOrderDetail s){
        return new SuppOrderDetailDTO(s.getId(),s.getProduct().getId(),s.getSuppOrder().getId(),s.getQuantity());
    }
}
