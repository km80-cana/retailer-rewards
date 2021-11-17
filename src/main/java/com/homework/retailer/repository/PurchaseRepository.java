package com.homework.retailer.repository;

import java.util.Date;
import java.util.List;

import com.homework.retailer.model.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Andres Canabal on 16/11/2021
 */

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    List<Purchase> findAllByDateBetween(Date from, Date to);

}
