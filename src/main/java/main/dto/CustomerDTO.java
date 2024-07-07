/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import java.util.List;
import main.entity.Address;

/**
 *
 * @author hp
 */
public record CustomerDTO(
            Integer id,
            String firstName,
            String lastName,
            Address address,
            String email,
            String phone,
            String pic,
            List<Integer> custOrderIds
        ) {
    
}
