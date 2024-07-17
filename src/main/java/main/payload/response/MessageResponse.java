/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.payload.response;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hp
 */
@Getter
@Setter
public class MessageResponse {

    public MessageResponse(String message) {
        this.message = message;
    }
    private String message;
}
