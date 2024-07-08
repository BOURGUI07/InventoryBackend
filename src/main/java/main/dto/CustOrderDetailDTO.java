/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotNull;
import main.entity.CustOrderDetailId;

/**
 *
 * @author hp
 */
public record CustOrderDetailDTO(
            CustOrderDetailId id,
            @NotNull(message="productId can't be null")
            Integer  productId,
            @NotNull(message="custOrderId can't be null")
            Integer custOrderId,
            Integer quantity
        ) {

}
