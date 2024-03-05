package com.msmeli.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sellers")
public class SellerRefactor extends UserEntity {
    private int meliID;
    private String refreshToken;
    private String tokenMl;
    @OneToMany(mappedBy = "sellerRefactor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;
    @OneToMany(mappedBy = "sellerRefactor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> Items;
    @ManyToMany
    @JoinTable(
            name = "Sellers_Suppliers",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private Set<Supplier> suppliers;

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
        supplier.getSellers().add(this);
    }


}
