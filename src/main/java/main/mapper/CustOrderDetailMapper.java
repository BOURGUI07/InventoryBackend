/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import main.dto.CustOrderDetailDTO;
import main.entity.CustOrderDetail;
import main.repo.CustOrderRepo;
import main.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CustOrderDetailMapper {
    @Autowired
    public CustOrderDetailMapper(ProductRepo repo, CustOrderRepo repo1) {
        this.repo = repo;
        this.repo1 = repo1;
    }
    private ProductRepo repo;
    private CustOrderRepo repo1;
    
    public CustOrderDetail toEntity(CustOrderDetailDTO x){
        var s = new CustOrderDetail();
        s.setQuantity(x.quantity());
        s.setProduct(repo.findById(x.productId()).orElse(null));
        s.setCustOrder(repo1.findById(x.custOrderId()).orElse(null));
        return s;
    }
    
    public CustOrderDetailDTO toDTO(CustOrderDetail s){
        return new CustOrderDetailDTO(s.getId(),s.getProduct().getId(),s.getCustOrder().getId(),s.getQuantity());
    }
}
