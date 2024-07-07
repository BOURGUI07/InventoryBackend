/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.mapper;

import java.util.stream.Collectors;
import main.dto.ProductDTO;
import main.entity.Product;
import main.repo.CategRepo;
import main.repo.CompanyRepo;
import main.repo.CustOrderDetailRepo;
import main.repo.SalesDetailRepo;
import main.repo.StockMvmRepo;
import main.repo.SuppOrderDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hp
 */
@Service
public class ProductMapper {
    @Autowired
    public ProductMapper(CategRepo categRepo, CompanyRepo companyRepo, CustOrderDetailRepo custRepo, SuppOrderDetailRepo suppRepo, StockMvmRepo stockRepo, SalesDetailRepo salesRepo) {
        this.categRepo = categRepo;
        this.companyRepo = companyRepo;
        this.custRepo = custRepo;
        this.suppRepo = suppRepo;
        this.stockRepo = stockRepo;
        this.salesRepo = salesRepo;
    }
    private CategRepo categRepo;
    private CompanyRepo companyRepo;
    private CustOrderDetailRepo custRepo;
    private SuppOrderDetailRepo suppRepo;
    private StockMvmRepo stockRepo;
    private SalesDetailRepo salesRepo;
    
    public Product toEntity(ProductDTO x){
        var  p = new Product();
        p.setCateg(categRepo.findById(x.categoryId()).orElse(null));
        p.setCompany(companyRepo.findById(x.companyId()).orElse(null));
        p.setDesc(x.desc());
        p.setName(x.name());
        p.setPic(x.pic());
        p.setPrice(x.price());
        p.setPriceTTC(x.priceTTC());
        p.setVatRate(x.vatRate());
        if(x.stockMvmIds()!=null){
            p.setStockMvms(stockRepo.findAllById(x.stockMvmIds()));
        }
        if(x.custOrderDetailIds()!=null){
            p.setCustOrderDetails(custRepo.findAllById(x.custOrderDetailIds()));
        }
        if(x.suppOrderDetailIds()!=null){
            p.setSuppOrderDetails(suppRepo.findAllById(x.suppOrderDetailIds()));
        }
        if(x.salesDetailIds()!=null){
            p.setSalesDetails(salesRepo.findAllById(x.salesDetailIds()));
        }
        return p;
    }
    
    public ProductDTO toDTO(Product p){
        var stockList = p.getStockMvms().stream().map(a -> a.getId()).collect(Collectors.toList());
        var custList = p.getCustOrderDetails().stream().map(a -> a.getId()).collect(Collectors.toList());
        var suppList = p.getSuppOrderDetails().stream().map(a -> a.getId()).collect(Collectors.toList());
        var salesList = p.getSalesDetails().stream().map(a -> a.getId()).collect(Collectors.toList());
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getDesc(),
                p.getPrice(),
                p.getVatRate(),
                p.getPriceTTC(),
                p.getPic(),
                p.getCateg().getId(),
                p.getCompany().getId(),
                stockList,
                salesList,
                custList,
                suppList            
        );
    }
}
