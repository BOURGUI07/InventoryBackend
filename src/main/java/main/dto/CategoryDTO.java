/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author hp
 */
public record CategoryDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotBlank(message = "category name is mandatory")
            @Size(min = 3, max = 50, message = "category name must be between 3 and 50 characters")
            String name,
            String desc,
            List<Integer> productIds
        ) {

}
