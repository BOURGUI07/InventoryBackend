/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import main.entity.SalesDetailId;

/**
 *
 * @author hp
 */
public record SalesDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotBlank(message = "sales code is mandatory")
            String code,
            List<SalesDetailId> salesDetailIds
        ) {

}
