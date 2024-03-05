package com.msmeli.repository;


import com.msmeli.model.SellerRefactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRefactorRepository extends JpaRepository<SellerRefactor, Long> {

    @Query("SELECT s.meliID FROM SellerRefactor s WHERE s.id= :userID" )
    Long GetMeliUserID (@Param("userID")Long UserID);

    @Query("SELECT s.tokenMl FROM SellerRefactor s WHERE s.id= :userID")
    String getMeliToken (@Param("userID")Long userID);
}
