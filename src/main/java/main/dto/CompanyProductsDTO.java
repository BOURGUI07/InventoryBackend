/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;

/**
 *
 * @author hp
 */
@AllArgsConstructor
public class CompanyProductsDTO {
    private Integer id;
    private String productName;
    private BigDecimal price;
    private String category;
}
