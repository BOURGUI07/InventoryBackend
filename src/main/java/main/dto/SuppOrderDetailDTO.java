/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotNull;
import main.entity.SuppOrderDetailId;

/**
 *
 * @author hp
 */
public record SuppOrderDetailDTO(
            SuppOrderDetailId id,
            @NotNull(message="productId  can't be null!")
            Integer  productId,
            @NotNull(message="suppOrderId  can't be null!")
            Integer suppOrderId,
            Integer quantity
        ) {

}
