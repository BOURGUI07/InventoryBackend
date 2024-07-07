/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.CustOrderDTO;
import main.entity.CustOrder;
import main.repo.CustOrderDetailRepo;
import main.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CustOrderMapper {
    @Autowired
    public CustOrderMapper(CustomerRepo repo, CustOrderDetailRepo repo1) {
        this.repo = repo;
        this.repo1 = repo1;
    }
    private CustomerRepo repo;
    private CustOrderDetailRepo repo1;
    
    public CustOrder toEntity(CustOrderDTO x){
        var s = new CustOrder();
        s.setCode(x.code());
        s.setCustomer(repo.findById(x.customerId()).orElse(null));
        s.setOrderDate(x.date());
        if(x.custOrderDetailIds()!=null){
            s.setCustOrderDetails(repo1.findAllById(x.custOrderDetailIds()));
        }
        return s;
    }
    
    public CustOrderDTO toDTO(CustOrder s){
        var list = s.getCustOrderDetails().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new CustOrderDTO(s.getId(),s.getCode(),s.getOrderDate(),s.getCustomer().getId() ,list);
    }
}
