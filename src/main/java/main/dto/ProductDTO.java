/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import java.math.BigDecimal;
import java.util.List;
import main.entity.CustOrderDetailId;
import main.entity.SalesDetailId;
import main.entity.SuppOrderDetailId;


/**
 *
 * @author hp
 */
public record ProductDTO(
        Integer id, 
        String name, 
        String desc,
        BigDecimal price,
        BigDecimal vatRate,
        BigDecimal priceTTC,
        String pic,
        Integer categoryId,
        Integer companyId,
        List<Integer> stockMvmIds,
        List<SalesDetailId> salesDetailIds,
        List<CustOrderDetailId> custOrderDetailIds,
        List<SuppOrderDetailId> suppOrderDetailIds
        ) {

}
