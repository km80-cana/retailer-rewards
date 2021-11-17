package com.homework.retailer.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * Created by Andres Canabal 14/11/21.
 */
@Entity
@Table(name = "purchase")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double value;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public void copyNotNullData(Purchase purchase) {

        if (Objects.nonNull(purchase.customerId))
            this.customerId = purchase.customerId;

        if (Objects.nonNull(purchase.description))
            this.description = purchase.description;

        if (Objects.nonNull(purchase.value))
            this.value = purchase.value;

        if (Objects.nonNull(purchase.date))
            this.date = purchase.date;        
    }
}
