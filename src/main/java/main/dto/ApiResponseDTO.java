/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dto;

import lombok.Data;

/**
 *
 * @author hp
 */
@Data
public class ApiResponseDTO<T> {

    public ApiResponseDTO(boolean isSuccess, String message, T resonse) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.resonse = resonse;
    }
    private boolean isSuccess;
    private String message;
    private T resonse;
}
