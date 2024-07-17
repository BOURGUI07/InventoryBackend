/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.payload.request;

import lombok.Data;

/**
 *
 * @author hp
 */
@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
