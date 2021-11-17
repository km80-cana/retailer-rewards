package com.homework.retailer.controller;

import java.util.List;

import javax.validation.Valid;

import com.homework.retailer.exception.ResourceNotFoundException;
import com.homework.retailer.model.Purchase;
import com.homework.retailer.repository.PurchaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Andres Canabal 14/11/21
 */
@RestController
@RequestMapping("/api")
public class PurchaseController {

    @Autowired
    PurchaseRepository purchaseRepository;

    @GetMapping("/purchase")
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        return ResponseEntity.ok().body(purchaseRepository.findAll());
    }

    @PostMapping("/purchase")
    public ResponseEntity<Purchase> createPurchase(@Valid @RequestBody Purchase purchase) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseRepository.save(purchase));
    }

    @GetMapping("/purchase/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable(value = "id") Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", "id", purchaseId));

        return ResponseEntity.ok().body(purchase);
    }

    @PutMapping("/purchase/{id}")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable(value = "id") Long purchaseId,
            @Valid @RequestBody Purchase purchaseNewData) {

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", "id", purchaseId));

        purchase.copyNotNullData(purchaseNewData);
        return ResponseEntity.ok().body(purchaseRepository.save(purchase));
    }

    @DeleteMapping("/purchase/{id}")
    public ResponseEntity<?> deletePurchase(@PathVariable(value = "id") Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", "id", purchaseId));

        purchaseRepository.delete(purchase);
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> notFound(ResourceNotFoundException exception) {
        throw exception;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> serverError(Exception exception) {    
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
