package com.msmeli.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.feign.MetricsDTO.SellerReputationDTO;
import com.msmeli.dto.request.SupplierRequestDTO;
import com.msmeli.dto.response.EmployeesResponseDto;
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/seller")
public class SellerController {
    private final SellerService sellerService;
    private final ItemService itemService;
    private final UserEntityService userEntityService;

    public SellerController(SellerService sellerService, ItemService itemService, UserEntityService userEntityService) {
        this.sellerService = sellerService;
        this.itemService = itemService;
        this.userEntityService = userEntityService;
    }

    @PostMapping("/tokenForTG")
    public void tokenForTg(@RequestParam String TG) {
        sellerService.saveToken(TG);
    }

    @PostMapping("/saveAllItemForSeller")
    public void saveAllItemForSeller() throws ResourceNotFoundException, AppException {
        itemService.saveAllItemForSeller();

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResposeDTO> refreshToken() {
        try {
            TokenResposeDTO refreshedToken = sellerService.refreshToken();
            return new ResponseEntity<>(refreshedToken, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Manejo de otras excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getEmployeesBySellerId")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesBySellerId() throws ResourceNotFoundException {
        List<EmployeesResponseDto> employeesList = sellerService.getEmployeesBySellerId();
        return ResponseEntity.ok(employeesList);
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<EmployeesResponseDto>> getAllEmployees() {
        List<EmployeesResponseDto> employeesList = sellerService.getAllEmployees();
        return ResponseEntity.ok(employeesList);
    }

    @PostMapping("/addSupplier")
    public ResponseEntity<String> addSupplier(@RequestBody SupplierRequestDTO supplierRequestDTO) throws AppException, ResourceNotFoundException {
        sellerService.addSupplier(supplierRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Supplier Agregado a Seller");
    }


    @GetMapping("getSellerRep")
    public ResponseEntity<SellerReputationDTO> getSellerReputation() throws JsonProcessingException, ResourceNotFoundException {
        SellerReputationDTO sellerRep = sellerService.getSellerReputation();
        return ResponseEntity.ok(sellerRep);
    }
    @GetMapping("/listSupplierSeller")
    public ResponseEntity<Set<Supplier>> listSupplierSeller () throws AppException, ResourceNotFoundException {
        Set<Supplier> listSupplier = sellerService.listSupplierSeller();
        return ResponseEntity.ok(listSupplier);

    }
}

