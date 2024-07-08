/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author hp
 */
public record UserDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotBlank(message = "user name is mandatory")
            @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
            String name,
            @NotNull(message="CompanyId  can't be null! User Has to Belong to a company.")
            Integer companyId
        ) {

}
