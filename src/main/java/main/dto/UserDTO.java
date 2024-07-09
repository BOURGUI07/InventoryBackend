/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import main.entity.Address;

/**
 *
 * @author hp
 */
public record UserDTO(
            @NotNull(message="Id can't be null")
            Integer id,
            @NotBlank(message = "first name is mandatory")
            @Size(min = 3, max = 50, message = "firstname must be between 3 and 50 characters")
            String firstName,
            @NotBlank(message = "last name is mandatory")
            @Size(min = 3, max = 50, message = "lastname must be between 3 and 50 characters")
            String lastName,
            Instant birthdate,
            @NotBlank(message="Password is mandatory")
            @Size(min = 3, max = 50, message = "password must be between 3 and 50 characters")
            String password,
            Address address,
            @Email(message="Email should be valid")
            String email,
            String pic,
            @NotNull(message="CompanyId  can't be null! User Has to Belong to a company.")
            Integer companyId,
            List<Integer> roleIds          
        ) {

}
