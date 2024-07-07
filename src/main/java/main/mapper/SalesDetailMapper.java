/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import main.dto.SalesDetailDTO;
import main.entity.SalesDetail;
import main.repo.ProductRepo;
import main.repo.SalesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SalesDetailMapper {
    @Autowired
    public SalesDetailMapper(ProductRepo repo, SalesRepo repo1) {
        this.repo = repo;
        this.salesRepo=repo1;
    }
    private ProductRepo repo;
    private SalesRepo salesRepo;
    
    public SalesDetail toEntity(SalesDetailDTO x){
        var s = new SalesDetail();
        s.setProduct(repo.findById(x.productId()).orElse(null));
        s.setSales(salesRepo.findById(x.salesId()).orElse(null));
        s.setQuantity(x.quantity());
        return s;
    }
    
    public SalesDetailDTO toDTO(SalesDetail s){
        return new SalesDetailDTO(s.getId(),s.getProduct().getId(),s.getSales().getId(),s.getQuantity());
    }
}
