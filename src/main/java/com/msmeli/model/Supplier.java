package com.msmeli.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String cuit;
    private String supplierName;
    private String domicilio;
    private String telefono;
    private String rubro;

    @JsonIgnore
    @ManyToMany(mappedBy = "suppliers")
    private Set<SellerRefactor> sellers;

    public void addSeller(SellerRefactor seller) {
        sellers.add(seller);
        seller.getSuppliers().add(this);
    }
}
