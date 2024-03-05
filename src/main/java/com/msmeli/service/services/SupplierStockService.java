package com.msmeli.service.services;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.request.SupplierStockRequestDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierStockService {
    void create(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException;
    SupplierStock createOrUpdateSupplierStock(SupplierStock supplierStock);

    SupplierStock findBySkuIdSeller(String sku, Long sellerId) throws AppException;
    Page<StockDTO> getStockAndSupplierStock(Long id, int offset, int pageSize) throws ResourceNotFoundException;
}
