package com.msmeli.controller;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.request.SupplierRequestDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.service.services.SupplierService;
import com.msmeli.service.services.SupplierStockService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meli/supplier")
public class SupplierController {


    private final SupplierService supplierService;
    private final SupplierStockService supplierStockService;

    public SupplierController(SupplierService supplierService, SupplierStockService supplierStockService) {
        this.supplierService = supplierService;
        this.supplierStockService = supplierStockService;
    }

    @PostMapping("/createStock")
    public ResponseEntity<String> createStock(@Valid @RequestBody StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        supplierStockService.create(stockBySupplierRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Stock Proveedor creado");
    }

    @GetMapping("/bySellerStockPaged")
    public ResponseEntity<Page<StockDTO>> getStockAndSupplierStock(@RequestParam("sellerId") Long sellerId, @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) throws ResourceNotFoundException, AppException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(supplierStockService.getStockAndSupplierStock(sellerId, offset, pageSize));
    }
    @PostMapping("/createSupplier")
    public ResponseEntity<String> createSupplier(@RequestBody SupplierRequestDTO supplierRequestDTO) throws AppException {
        supplierService.createSupplier(supplierRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Proveedor Creado");
    }
    @GetMapping("/listSupplier")
        public ResponseEntity<List<Supplier>> listSupplier() throws AppException {
            return ResponseEntity.status(HttpStatus.OK).body(supplierService.listSupplier());
        }

   @PutMapping("/editSupplier/{id}")
   public ResponseEntity<String> editSupplier(@PathVariable Integer id, @RequestBody SupplierRequestDTO supplierResquestDTO) throws ResourceNotFoundException, AppException {
       supplierService.editSupplier(id, supplierResquestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Proveedor actualizado");
    }


    @DeleteMapping("/deleteSupplier/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Integer id) throws ResourceNotFoundException {
        supplierService.deleteSupplier(id);
        return ResponseEntity.status(HttpStatus.OK).body("Proveedor eliminado");
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<Supplier> findById(@PathVariable Integer id) throws ResourceNotFoundException {
        Supplier supplier = supplierService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(supplier);
    }

}