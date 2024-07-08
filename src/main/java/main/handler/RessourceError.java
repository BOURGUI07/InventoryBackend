/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hp
 */
@Getter
@Setter
@AllArgsConstructor
public class RessourceError {
    private int status;
    private String message;
    private long timestamp;
}
