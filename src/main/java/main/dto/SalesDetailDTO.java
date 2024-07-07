/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import main.entity.SalesDetailId;

/**
 *
 * @author hp
 */
public record SalesDetailDTO(
            SalesDetailId id,
            Integer productId,
            Integer salesId,
            Integer quantity
        ) {

}
