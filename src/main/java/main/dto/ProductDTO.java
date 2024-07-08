/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        @NotNull(message="Id can't be null")
        Integer id, 
        @NotBlank(message = "product name is mandatory")
        @Size(min = 3, max = 50, message = "product name must be between 3 and 50 characters")
        String name, 
        String desc,
        BigDecimal price,
        BigDecimal vatRate,
        BigDecimal priceTTC,
        String pic,
        @NotNull(message="CategoryId can't be null. Product has to belong to a category")
        Integer categoryId,
        @NotNull(message="CompanyId can't be null. Product has to belong to a company")
        Integer companyId,
        List<Integer> stockMvmIds,
        List<SalesDetailId> salesDetailIds,
        List<CustOrderDetailId> custOrderDetailIds,
        List<SuppOrderDetailId> suppOrderDetailIds
        ) {

}
