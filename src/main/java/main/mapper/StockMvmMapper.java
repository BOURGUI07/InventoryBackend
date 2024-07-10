/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import main.dto.StockMvmDTO;
import main.entity.StockMvm;
import main.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class StockMvmMapper {
    @Autowired
    public StockMvmMapper(ProductRepo repo) {
        this.repo = repo;
    }
    private ProductRepo repo;
    
    public StockMvm toEntity(StockMvmDTO x){
        var s = new StockMvm();
        s.setDestinationLocation(x.destination());
        s.setMovementDate(x.date());
        s.setMovementType(x.type());
        s.setSourceLocation(x.source());
        s.setQty(x.qty());
        s.setProduct(repo.findById(x.productId()).orElse(null));
        return s;
    }
    
    public StockMvmDTO toDTO(StockMvm s){
        return new StockMvmDTO(s.getId(),s.getQty(),s.getMovementType(),s.getMovementDate(),s.getSourceLocation(),s.getDestinationLocation(), s.getProduct().getId());
    }
}
