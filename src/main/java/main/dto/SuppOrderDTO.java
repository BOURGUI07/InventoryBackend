/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import main.entity.SuppOrderDetailId;

/**
 *
 * @author hp
 */
public record SuppOrderDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotBlank(message = "order code is mandatory")
            String code,
            Instant date,
            @NotNull(message="supplierId  can't be null!")
            Integer supplierId,
            List<SuppOrderDetailId> suppOrderDetailIds
        ) {

}
