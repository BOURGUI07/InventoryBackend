/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.CustomerDTO;
import main.entity.Customer;
import main.repo.CustOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class CustomerMapper {
    @Autowired
    public CustomerMapper(CustOrderRepo repo) {
        this.repo = repo;
    }
    private CustOrderRepo repo;
    public Customer toEntity(CustomerDTO x){
        var c = new Customer();
        c.setAddress(x.address());
        c.setEmail(x.email());
        c.setPic(x.pic());
        c.setPhone(x.phone());
        c.setFirstName(x.firstName());
        c.setLastName(x.lastName());
        if(x.custOrderIds()!=null){
            c.setCustOrders(repo.findAllById(x.custOrderIds()));
        }
        return c;
    }
    
    public CustomerDTO toDTO(Customer c){
        var list = c.getCustOrders().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new CustomerDTO(c.getId(),c.getFirstName(),c.getLastName(),c.getAddress(),c.getEmail(),c.getPhone(),c.getPic(),list);
    }
}
