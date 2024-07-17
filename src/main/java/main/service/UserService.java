/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.service;


import main.entity.User;

import main.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class UserService {
    @Autowired
    public UserService(UserRepo repo) {
        this.repo = repo;
    }
    private UserRepo repo;
    
    public Boolean existsByUsername(String username){
        return repo.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email){
        return repo.existsByEmail(email);
    }
    
    public void save(User user){
        repo.save(user);
    }
}
