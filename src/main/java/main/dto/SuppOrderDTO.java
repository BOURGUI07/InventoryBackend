/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package main.dto;

import java.time.Instant;
import java.util.List;
import main.entity.SuppOrderDetailId;

/**
 *
 * @author hp
 */
public record SuppOrderDTO(
            Integer id,
            String code,
            Instant date,
            Integer supplierId,
            List<SuppOrderDetailId> suppOrderDetailIds
        ) {

}
