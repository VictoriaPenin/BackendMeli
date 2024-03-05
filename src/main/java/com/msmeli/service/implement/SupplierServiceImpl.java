package com.msmeli.service.implement;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.request.SupplierRequestDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SellerRefactor;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.repository.SupplierRepository;
import com.msmeli.service.services.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    private final ModelMapper modelMapper;



    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;

        this.modelMapper = modelMapper;
    }

    @Override
    public List<SupplierStock> uploadSupplierStock(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        return null;
    }

    public Supplier findById(Integer id) throws ResourceNotFoundException {
        return supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No supplier"));
    }



    /**
     * Metodo se encarga en crear/declarar Supplier en relacion de seller logueado
     */

    @Override
    @Transactional
    public void createSupplier(SupplierRequestDTO supplierRequestDTO) throws AppException {
        try {
            if (hasSupplier(supplierRequestDTO.getId())) {
                Supplier newSupplier = modelMapper.map(supplierRequestDTO, Supplier.class);
                supplierRepository.save(newSupplier);
            }
        } catch (Exception ex) {
            ex.printStackTrace();  // Log the exception details
            throw new AppException("Error en la creacion de Proveedor", "CreateSupplier", 000, 500);
        }

    }


    /**
     * Este metodo devuelve todas la entidades Supplier
     *
     * @return lista de Entidad Supplier
     * @throws AppException
     */
    @Override
    public List<Supplier> listSupplier() throws AppException {
        List<Supplier> listSupplier = supplierRepository.findAll();
        if (listSupplier.isEmpty()) throw new AppException("No hay supplier en el sistema", "listSupplier", 000, 500);
        return listSupplier;
    }

    @Override
    @Transactional
    public void editSupplier(Integer id, SupplierRequestDTO supplierRequestDTO) throws ResourceNotFoundException, AppException {
        Supplier existingSupplier = findById(id);

        if (supplierRequestDTO.getCuit() != null) {
            existingSupplier.setCuit(supplierRequestDTO.getCuit());
        }
        if (supplierRequestDTO.getSupplierName() != null) {
            existingSupplier.setSupplierName(supplierRequestDTO.getSupplierName());
        }
        if (supplierRequestDTO.getDomicilio() != null) {
            existingSupplier.setDomicilio(supplierRequestDTO.getDomicilio());
        }
        if (supplierRequestDTO.getTelefono() != null) {
            existingSupplier.setTelefono(supplierRequestDTO.getTelefono());
        }
        if (supplierRequestDTO.getRubro() != null) {
            existingSupplier.setRubro(supplierRequestDTO.getRubro());
        }


        supplierRepository.save(existingSupplier);
    }



    /**
     * Este metodo se encarga de devolver un True si no existe un Proveedor con el id enviado por parametro
     *
     * @param id para Identificar la entidad en la base de datos
     * @return devuelve true si no se encontro ninguna entidad
     * @throws AppException
     */
    private boolean hasSupplier(Integer id) throws AppException {
        if (!supplierRepository.existsById(id)) {
            return true;
        } else throw new AppException("Ya existe un proveedor con ese cuit", "createSupplier->hasSupplier", 000, 500);
    }
//@Override
//    @Transactional
//    public SupplierRequestDTO updateSupplier(Integer supplierId, SupplierRequestDTO supplierUpdateDTO)
//            throws ResourceNotFoundException, AlreadyExistsException {
//        // Obtener el proveedor existente por su ID
//        Supplier existingSupplier = supplierRepository.findById(supplierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + supplierId));
//
//        // Validar si el nuevo CUIT ya existe
//        if (!existingSupplier.getCuit().equals(supplierUpdateDTO.getCuit())
//                && supplierRepository.findById(supplierUpdateDTO.getId()).isPresent()) {
//            throw new AlreadyExistsException("El CUIT ya existe.");
//        }
//
//        // Actualizar solo los campos proporcionados en la solicitud
//        if (supplierUpdateDTO.getCuit() != null) {
//            existingSupplier.setCuit(supplierUpdateDTO.getCuit());
//        }
//        if (supplierUpdateDTO.getSupplierName() != null) {
//            existingSupplier.setSupplierName(supplierUpdateDTO.getSupplierName());
//        }
//        if (supplierUpdateDTO.getDomicilio() != null) {
//            existingSupplier.setDomicilio(supplierUpdateDTO.getDomicilio());
//        }
//        if (supplierUpdateDTO.getTelefono() != null) {
//            existingSupplier.setTelefono(supplierUpdateDTO.getTelefono());
//        }
//        if (supplierUpdateDTO.getRubro() != null) {
//            existingSupplier.setRubro(supplierUpdateDTO.getRubro());
//        }
//
//        // Guardar la entidad actualizada
//        supplierRepository.save(existingSupplier);
//
//        // Mapear la entidad a DTO y devolver la respuesta
//        return modelMapper.map(existingSupplier, SupplierRequestDTO.class);
//    }


    @Override
    @Transactional
    public void deleteSupplier(Integer id) throws ResourceNotFoundException {
        Supplier existingSupplier = findById(id);

        for (SellerRefactor seller : existingSupplier.getSellers()) {
            seller.getSuppliers().remove(existingSupplier);
        }

        supplierRepository.deleteById(id);
    }



}


