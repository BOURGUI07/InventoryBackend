/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author hp
 */
public record StockMvmDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotNull(message="ProductId can't be null. StockMvm has to belong to a product")
            Integer productId
        ) {

}
