package com.msmeli.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequestDTO {
    @NotNull
    private int id;
    @NotBlank
    @NotNull
    private String cuit;
    private String supplierName;
    private String domicilio;
    private String telefono;
    private String rubro;

}
