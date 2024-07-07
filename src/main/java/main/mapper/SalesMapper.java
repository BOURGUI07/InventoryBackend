/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.SalesDTO;
import main.entity.Sales;
import main.repo.SalesDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class SalesMapper {
    @Autowired
    public SalesMapper(SalesDetailRepo repo) {
        this.repo = repo;
    }
    private SalesDetailRepo repo;
    
    public Sales toEntity(SalesDTO x){
        var s = new Sales();
        s.setCode(x.code());
        if(x.salesDetailIds()!=null){
            s.setSalesDetails(repo.findAllById(x.salesDetailIds()));
        }
        return s;
    }
    
    public SalesDTO toDTO(Sales s){
        var list = s.getSalesDetails().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new SalesDTO(s.getId(),s.getCode(),list);
    }
}
