package com.homework.retailer.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.homework.retailer.model.Purchase;
import com.homework.retailer.model.RewardPoints;
import com.homework.retailer.repository.PurchaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Andres Canabal 14/11/21
 */
@RestController
@RequestMapping("/api")
public class RewardController {

    private static final int LIMIT_1 = 50;
    private static final int POINTS_1 = 1;
    private static final int LIMIT_2 = 100;
    private static final int POINTS_2 = 2;


    @Autowired
    PurchaseRepository purchaseRepository;
                  
    @GetMapping("/rewards")
    public ResponseEntity<RewardPoints> getRewardPoints() throws IOException {
        Calendar calendar = Calendar.getInstance(); 
        Date to = calendar.getTime();
        calendar.add(Calendar.MONTH, -3);        
        Date from = calendar.getTime();
 
        RewardPoints rewardPoints = RewardPoints.builder().to(to).from(from).build();
        List<Purchase> purchases = purchaseRepository.findAllByDateBetween(from, to);
        purchases.forEach(purchase -> rewardPoints.add(purchase.getCustomerId(), calculatePoints(purchase))); 

        return ResponseEntity.ok().body(rewardPoints);
    }

    private int calculatePoints(Purchase purchase) {
        int value = purchase.getValue().intValue();
        if (value > LIMIT_2) {
            return (LIMIT_2 - LIMIT_1) * POINTS_1 + (value - LIMIT_2) * POINTS_2;
        }
        if (value > LIMIT_1) {
            return (value - LIMIT_1) * POINTS_1;
        }
        return 0;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> serverError(Exception exception) {
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
