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

/**
 *
 * @author hp
 */
public record UserDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotBlank(message = "first name is mandatory")
            @Size(min = 3, max = 50, message = "firstname must be between 3 and 50 characters")
            String username,
            @NotBlank(message="Password is mandatory")
            @Size(min = 3, max = 50, message = "password must be between 3 and 50 characters")
            String password,
            @Email(message="Email should be valid")
            String email,
            boolean enabled,
            @NotNull(message="CompanyId  can't be null! User Has to Belong to a company.")
            Integer companyId,
            List<Integer> roleIds          
        ) {

}
