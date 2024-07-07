/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import main.entity.CustOrderDetailId;

/**
 *
 * @author hp
 */
public record CustOrderDetailDTO(
            CustOrderDetailId id,
            Integer  productId,
            Integer custOrderId,
            Integer quantity
        ) {

}
