package com.msmeli.service.implement;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.request.SupplierStockRequestDTO;
import com.msmeli.dto.response.SupplierStockResponseDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SellerRefactor;
import com.msmeli.model.Stock;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.repository.SupplierStockRepository;
import com.msmeli.service.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierStockServiceImpl implements SupplierStockService {

    private final SupplierStockRepository supplierStockRepository;
    private final UserEntityService userEntityService;
    private final ModelMapper mapper;
    private final SellerService sellerService;
    private final SupplierService supplierService;
    private final StockService stockService;

    public SupplierStockServiceImpl(SupplierStockRepository supplierStockRepository, ModelMapper mapper, UserEntityService userEntityService, SupplierService supplierService, SellerService sellerService, StockService stockService) {
        this.supplierStockRepository = supplierStockRepository;
        this.mapper = mapper;
        this.userEntityService = userEntityService;
        this.sellerService = sellerService;
        this.supplierService = supplierService;
        this.stockService = stockService;
    }

    @Override
    public SupplierStock createOrUpdateSupplierStock(SupplierStock supplierStock) {
        supplierStock.setPrice(Math.round(supplierStock.getPrice() * 100.0) / 100.0);
        return supplierStockRepository.save(supplierStock);
    }

    /**
     * Este metodo se encarga de traer todos los registro de stock del proveedor en relacion con el seller y el sku
     * @param sku sku del item para buscar su equivalenten en stock proveedor
     * @param sellerId id del seller dueño de los registros
     * @return registro del stock de proveedor
     * @throws AppException
     */
    @Override
    public SupplierStock findBySkuIdSeller(String sku, Long sellerId) throws AppException {
        try {
            SupplierStock supplierStock = supplierStockRepository.findbySkuIdSeller(sku,sellerId);
            return supplierStock;
        }catch (Exception e){
            throw new AppException("Error en la busqueda del stockSupplier", "SupplierStockServiceImpl->findBySkuIdSeller",000,503);
        }
    }

    /**
     * Este metodo se encarga en crear el Stock de un provedero identificado mediiante el id que esta en el DTO
     * en relacion al seller solicitiante
     * @param stockBySupplierRequestDTO
     */
    @Override
    @Transactional
    public void create(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        Long idSeller = userEntityService.getAuthenticatedUserId();
        SellerRefactor seller = sellerService.findById(idSeller);
        Supplier supplier = supplierService.findById(stockBySupplierRequestDTO.getSupplierId());


        stockBySupplierRequestDTO.getContent().forEach(supplierStockDTO -> {
            setSupplierStock(supplierStockDTO,supplier,seller);
        });
    }


    /**
     * Este metodo se encarga de setear los datos a la entidad SupplierStock
     * @param supplierStockDTO Dto con los datos del stock
     * @param supplier proveedor a setear en SupplierStock (relaccionar)
     * @param seller vendedor a setear en SupplierStock (relaccionar)
     */
    private void setSupplierStock(SupplierStockRequestDTO supplierStockDTO,Supplier supplier, SellerRefactor seller){
        SupplierStock supplierStock = new SupplierStock();
        supplierStock.setSupplier(supplier);
        supplierStock.setSeller(seller);
        supplierStock.setPrice(supplierStockDTO.getPrice());
        supplierStock.setAvailableQuantity(supplierStockDTO.getAvailableQuantity());
        supplierStock.setSku(supplierStockDTO.getSku());

        supplierStockRepository.save(supplierStock);
    }

    /**
     * Metodo se encaga de empaquetar  el stock del seller invocando setStockSupplierInStockDTO para poder completar los
     * datos de StockDTO con la informacion del stock de seller y stock supplier
     * @param id (NO VA MAS)
     * @param offset
     * @param pageSize
     * @return
     * @throws ResourceNotFoundException
     */
    @Override
    public Page<StockDTO> getStockAndSupplierStock(Long id, int offset, int pageSize) throws ResourceNotFoundException{
        Long idSeller = userEntityService.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Stock> sellerStock = stockService.findAllPaged(idSeller, pageable);

        return setStockSupplierInStockDTO(pageable, sellerStock, idSeller);
    }

    /**
     * Metodo se encarga de setear los datos de Supplier Stock en el StockDTO
     * @param pageable
     * @param stockPage porcion de los datos seller stock
     * @param sellerId Id del seller dueño de los registos
     * @return
     */
    private Page<StockDTO> setStockSupplierInStockDTO(Pageable pageable, Page<Stock> stockPage, Long sellerId)  {

        List<StockDTO> stock = stockPage.stream().map(e -> {
            try {
                StockDTO stockDTO = mapper.map(e, StockDTO.class);
                SupplierStock supplierStock = findBySkuIdSeller(stockDTO.getSku(),sellerId);
                if(supplierStock != null){
                    SupplierStockResponseDTO supplierStockResponseDTO = mapper.map(supplierStock,SupplierStockResponseDTO.class);
                    supplierStockResponseDTO.setNickname(supplierStock.getSupplier().getSupplierName());
                    stockDTO.setSupplierContent(supplierStockResponseDTO);
                }

                return stockDTO;

            } catch (AppException ex) {
                throw new RuntimeException(ex);
            }
        }).toList();
        return new PageImpl<>(stock, pageable, stockPage.getTotalElements());
    }
}
