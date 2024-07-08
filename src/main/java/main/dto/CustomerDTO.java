/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import main.entity.Address;

/**
 *
 * @author hp
 */
public record CustomerDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotBlank(message = "first name is mandatory")
            @Size(min = 3, max = 50, message = "firstname must be between 3 and 50 characters")
            String firstName,
            @NotBlank(message = "last name is mandatory")
            @Size(min = 3, max = 50, message = "lastname must be between 3 and 50 characters")
            String lastName,
            Address address,
            @Email(message="Email should be valid")
            String email,
            String phone,
            String pic,
            List<Integer> custOrderIds
        ) {
    
}
