/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.payload.request;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hp
 */
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
