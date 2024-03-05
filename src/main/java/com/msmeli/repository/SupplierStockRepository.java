package com.msmeli.repository;

import com.msmeli.model.SupplierStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierStockRepository extends JpaRepository<SupplierStock, Long> {
    Optional<SupplierStock> findBySku(String sku);
    @Query("SELECT s FROM SupplierStock s WHERE s.sku = :sku and s.seller.id = :IdSeller")
    SupplierStock findbySkuIdSeller(String sku,Long IdSeller);
}
