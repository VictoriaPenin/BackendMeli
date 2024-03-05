package com.msmeli.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.model.Supplier;
import com.msmeli.repository.RoleRepository;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.SupplierRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DataInitializer {

    private final SellerService sellerService;
    private final SupplierRepository supplierRepository;
    private final RoleRepository roleRepository;
    private final StockRepository stockRepository;
    private final UserEntityRepository userEntityRepository;
    private final StockService stockService;
    private final ItemService itemService;
    private final AllGeneralCategoryService generalCategoryService;

    public DataInitializer(SellerService sellerService, SupplierRepository supplierRepository, RoleRepository roleRepository, StockRepository stockRepository, UserEntityRepository userEntityRepository, StockService stockService, ItemService itemService, AllGeneralCategoryService generalCategoryService) {
        this.sellerService = sellerService;
        this.supplierRepository = supplierRepository;
        this.roleRepository = roleRepository;
        this.stockRepository = stockRepository;
        this.userEntityRepository = userEntityRepository;
        this.stockService = stockService;
        this.itemService = itemService;
        this.generalCategoryService = generalCategoryService;
    }


    public Supplier defaultSupplier() {
        Supplier supplier = null;
        if (supplierRepository.findAll().isEmpty()) {
            supplier = new Supplier();
            supplier.setSupplierName("Kalydon Tools");
            supplier = supplierRepository.save(supplier);
        }
        return supplier;
    }


   /* public void defaultUser() throws AlreadyExistsException, ResourceNotFoundException {
        if (userEntityRepository.findAll().isEmpty()) {
            sellerService.createSeller(new UserRegisterRequestDTO("user1", "123456", "123456", "mt.soporte.usuario@gmail.com", 1));
        }
    }*/

    public void defaultRoles() {
        if (roleRepository.findAll().isEmpty()) {
            for (Role role : Role.values()) {
                roleRepository.save(RoleEntity.builder().name(role).build());
            }
        }
    }
    public void cargarCategorias() throws AppException, ResourceNotFoundException {
        generalCategoryService.saveAllGeneralCategory();
    }

    public void defaulStock() throws IOException {
        if (stockRepository.findAll().isEmpty()) {
            ClassPathResource resource = new ClassPathResource("stock_example.json");
            ObjectMapper map = new ObjectMapper();
            StockRequestDTO requestDTO = map.readValue(resource.getInputStream(), StockRequestDTO.class);
            stockService.saveSellerStock(requestDTO);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(5)
    public void fillBd() throws AlreadyExistsException, ResourceNotFoundException, IOException, AppException {


        defaultRoles();
        cargarCategorias();



    }
}
