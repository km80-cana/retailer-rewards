package com.homework.retailer.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RewardPoints {    

    private Date from;
    
    private Date to;
    
    @Builder.Default
    private Map<Long, Integer> customerPoints = new HashMap<Long, Integer>();
    

    public void add(long customerId, int points) {      
        if (customerPoints.containsKey(customerId)) {
            points += customerPoints.get(customerId);
        }
        customerPoints.put(customerId, points);
    }

    public long getPoints(long customerId) {
        if (!customerPoints.containsKey(customerId)) {
            return 0;
        }
        return customerPoints.get(customerId);
    }

}
