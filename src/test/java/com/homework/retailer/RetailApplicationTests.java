package com.homework.retailer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.homework.retailer.model.Purchase;
import com.homework.retailer.model.RewardPoints;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RetailApplicationTests {

	@LocalServerPort
	private int port;	
	
	@Autowired
	private TestRestTemplate restTemplate;

	private final String API_PURCHASE = "api/purchase";
	private final String API_REWARDS = "api/rewards";

	@Test
	public void basic() throws Exception {
		String baseUrl = "http://localhost:" + port + "/";		

		// Initialize Variables
		ResponseEntity<RewardPoints> initialRewardEntity = restTemplate.getForEntity(baseUrl + API_REWARDS, RewardPoints.class);
		assertThat(initialRewardEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		RewardPoints initialRewardPoints = initialRewardEntity.getBody();

		Long newCustomerId = 100l;				
		while(initialRewardPoints.getPoints(newCustomerId) != 0) {
			newCustomerId++;
		}
		Long otherNewCustomerId = newCustomerId + 1;				
		while(initialRewardPoints.getPoints(otherNewCustomerId) != 0) {
			otherNewCustomerId++;
		}

		ResponseEntity<List> initialPurchaseCountEntity = restTemplate.getForEntity(baseUrl + API_PURCHASE, List.class);
		assertThat(initialPurchaseCountEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		int initialPurchaseCount = initialPurchaseCountEntity.getBody().size();

				
		// Create New Purchase : $120 => 90 points
		Purchase firstPurchase = new Purchase(null, newCustomerId, "description", 120d, new Date());

		ResponseEntity<Purchase> purchaseEntenty = restTemplate.postForEntity(baseUrl + API_PURCHASE, firstPurchase, Purchase.class);				
		Purchase pReturn = purchaseEntenty.getBody();

		assertThat(purchaseEntenty.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(firstPurchase.getId()).isNull();
		assertThat(pReturn.getId()).isNotNull();
		assertCompare(firstPurchase, pReturn, false);


		purchaseEntenty = restTemplate.getForEntity(baseUrl + API_PURCHASE + "/" + pReturn.getId(), Purchase.class);		
		firstPurchase = purchaseEntenty.getBody();
		assertThat(purchaseEntenty.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertCompare(firstPurchase, pReturn, true);
				
		assertThat(restTemplate.getForObject(baseUrl + API_PURCHASE, List.class).size()).isEqualTo(initialPurchaseCount + 1);	
		assertThat(restTemplate.getForObject(baseUrl + API_REWARDS, RewardPoints.class).getPoints(newCustomerId)).isEqualTo(90);		

		
		// Modify First Purchase
		String modifyUrl = baseUrl + API_PURCHASE + "/" + firstPurchase.getId();

		Purchase pCustomer = new Purchase(null, otherNewCustomerId, null, null, null);
		restTemplate.put(modifyUrl, pCustomer);		

		Purchase pDescription = new Purchase(null, null, "new description", null, null);
		restTemplate.put(modifyUrl, pDescription);		
		
		// $70 => 20 points
		Purchase pValue = new Purchase(null, null, null, 70d, null);
		restTemplate.put(modifyUrl, pValue);		

		Calendar calendar2Month = Calendar.getInstance();         
        calendar2Month.add(Calendar.MONTH, -2);        		
		Purchase pDate = new Purchase(null, null, null, null, calendar2Month.getTime());
		restTemplate.put(modifyUrl, pDate);		

		purchaseEntenty = restTemplate.getForEntity(modifyUrl, Purchase.class);	
		firstPurchase = purchaseEntenty.getBody();
		assertThat(purchaseEntenty.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(firstPurchase.getCustomerId()).isEqualTo(pCustomer.getCustomerId());	
		assertThat(firstPurchase.getDescription()).isEqualTo(pDescription.getDescription());	
		assertThat(firstPurchase.getValue()).isEqualTo(pValue.getValue());	
		assertThat(firstPurchase.getDate()).isCloseTo(pDate.getDate(), 1000);

		assertThat(restTemplate.getForObject(baseUrl + API_PURCHASE, List.class).size()).isEqualTo(initialPurchaseCount + 1);	
		RewardPoints rewardPoints = restTemplate.getForObject(baseUrl + API_REWARDS, RewardPoints.class);
		assertThat(rewardPoints.getPoints(newCustomerId)).isEqualTo(0);		
		assertThat(rewardPoints.getPoints(otherNewCustomerId)).isEqualTo(20);	
		

		// Create more Purchase and check Points
		Purchase purchaseA = new Purchase(null, newCustomerId, "adds 150 points", 150.2d, new Date());
		purchaseA = restTemplate.postForObject(baseUrl + API_PURCHASE, purchaseA, Purchase.class);

		Purchase purchaseB = new Purchase(null, newCustomerId, "adds 15 points, 2 month old", 65.9d , calendar2Month.getTime());
		purchaseB = restTemplate.postForObject(baseUrl + API_PURCHASE, purchaseB, Purchase.class);	

		Purchase purchaseC = new Purchase(null, newCustomerId, "adds 0 points, value to low", 34d , new Date());
		purchaseC = restTemplate.postForObject(baseUrl + API_PURCHASE, purchaseC, Purchase.class);	

		Purchase purchaseOtherCustomer = new Purchase(null, otherNewCustomerId, "adds 50 point to otherCostumer", 100d , new Date());
		purchaseOtherCustomer = restTemplate.postForObject(baseUrl + API_PURCHASE, purchaseOtherCustomer, Purchase.class);	

		Calendar calendar4Month = Calendar.getInstance();         
        calendar4Month.add(Calendar.MONTH, -4);        		
		Purchase purchaseTooOld = new Purchase(null, newCustomerId, "adds 0 points, too old", 100d , calendar4Month.getTime());
		purchaseTooOld = restTemplate.postForObject(baseUrl + API_PURCHASE, purchaseTooOld, Purchase.class);	


		assertThat(restTemplate.getForObject(baseUrl + API_PURCHASE, List.class).size()).isEqualTo(initialPurchaseCount + 6);	
		rewardPoints = restTemplate.getForObject(baseUrl + API_REWARDS, RewardPoints.class);
		assertThat(rewardPoints.getPoints(newCustomerId)).isEqualTo(165);		
		assertThat(rewardPoints.getPoints(otherNewCustomerId)).isEqualTo(70);	


		// Delete Created Purchases
		restTemplate.delete(baseUrl + API_PURCHASE + "/" + firstPurchase.getId());		
		restTemplate.delete(baseUrl + API_PURCHASE + "/" + purchaseA.getId());
		restTemplate.delete(baseUrl + API_PURCHASE + "/" + purchaseB.getId());
		restTemplate.delete(baseUrl + API_PURCHASE + "/" + purchaseC.getId());
		restTemplate.delete(baseUrl + API_PURCHASE + "/" + purchaseOtherCustomer.getId());
		restTemplate.delete(baseUrl + API_PURCHASE + "/" + purchaseTooOld.getId());

		assertThat(restTemplate.getForObject(baseUrl + API_PURCHASE, List.class).size()).isEqualTo(initialPurchaseCount);
		rewardPoints = restTemplate.getForObject(baseUrl + API_REWARDS, RewardPoints.class);
		assertThat(rewardPoints.getPoints(newCustomerId)).isEqualTo(0);		
		assertThat(rewardPoints.getPoints(otherNewCustomerId)).isEqualTo(0);


		// Check Return Error Codes
		String notFoundUrl = baseUrl + API_PURCHASE + "/" + firstPurchase.getId();
		
		purchaseEntenty = restTemplate.exchange(notFoundUrl, HttpMethod.GET, new HttpEntity<Purchase>(firstPurchase), Purchase.class);
		assertThat(purchaseEntenty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		purchaseEntenty = restTemplate.exchange(notFoundUrl, HttpMethod.PUT, new HttpEntity<Purchase>(firstPurchase), Purchase.class);
		assertThat(purchaseEntenty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		purchaseEntenty = restTemplate.exchange(notFoundUrl, HttpMethod.DELETE, new HttpEntity<Purchase>(firstPurchase), Purchase.class);
		assertThat(purchaseEntenty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private void assertCompare(Purchase pa, Purchase pb, boolean includeId) {
		if (includeId) 
			assertThat(pa.getId()).isEqualTo(pb.getId());
		assertThat(pa.getCustomerId()).isEqualTo(pb.getCustomerId());
		assertThat(pa.getDescription()).isEqualTo(pb.getDescription());
		assertThat(pa.getValue()).isEqualTo(pb.getValue());		
		assertThat(pa.getDate()).isCloseTo(pb.getDate(), 1000);
	}
}