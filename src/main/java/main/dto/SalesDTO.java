/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import java.util.List;
import main.entity.SalesDetailId;

/**
 *
 * @author hp
 */
public record SalesDTO(
            Integer id,
            String code,
            List<SalesDetailId> salesDetailIds
        ) {

}
