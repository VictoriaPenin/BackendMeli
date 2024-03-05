package com.msmeli.service.services;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.request.SupplierRequestDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SupplierService {

    List<SupplierStock> uploadSupplierStock(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException;

    Supplier findById(Integer id) throws ResourceNotFoundException;

    void createSupplier(SupplierRequestDTO supplierRequestDTO) throws AppException;

    List<Supplier> listSupplier() throws AppException;

    void editSupplier(Integer id, SupplierRequestDTO supplierRequestDTO) throws ResourceNotFoundException, AppException;


    void deleteSupplier(Integer id) throws ResourceNotFoundException;


}
