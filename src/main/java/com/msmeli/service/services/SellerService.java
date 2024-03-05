package com.msmeli.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.feign.MetricsDTO.SellerReputationDTO;
import com.msmeli.dto.request.EmployeeRegisterRequestDTO;
import com.msmeli.dto.request.SupplierRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.dto.response.EmployeesResponseDto;
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.dto.response.UserResponseDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SellerRefactor;
import com.msmeli.model.Supplier;
import java.util.List;
import java.util.Set;

public interface SellerService {
    UserResponseDTO createSeller(UserRegisterRequestDTO userRegisterRequestDTO) throws ResourceNotFoundException, AlreadyExistsException, AppException;
    UserResponseDTO createEmployee(EmployeeRegisterRequestDTO EmployeeRegister) throws ResourceNotFoundException, AlreadyExistsException;
    TokenResposeDTO saveToken(String TG);
    SellerRefactor findById(Long id) throws ResourceNotFoundException;
    TokenResposeDTO refreshToken();
    List<EmployeesResponseDto> getEmployeesBySellerId() throws ResourceNotFoundException;
    List<EmployeesResponseDto> getAllEmployees();
    void addSupplier(SupplierRequestDTO supplierRequestDTO) throws ResourceNotFoundException, AppException;
    SellerReputationDTO getSellerReputation() throws JsonProcessingException, ResourceNotFoundException;
    Set<Supplier> listSupplierSeller() throws ResourceNotFoundException, AppException;
}
