/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import main.entity.SuppOrderDetailId;

/**
 *
 * @author hp
 */
public record SuppOrderDetailDTO(
            SuppOrderDetailId id,
            Integer  productId,
            Integer suppOrderId,
            Integer quantity
        ) {

}
