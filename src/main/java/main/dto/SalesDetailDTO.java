/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotNull;
import main.entity.SalesDetailId;

/**
 *
 * @author hp
 */
public record SalesDetailDTO(
            SalesDetailId id,
            @NotNull(message="productId can't be null")
            Integer productId,
            @NotNull(message="SalesId can't be null")
            Integer salesId,
            Integer quantity
        ) {

}
