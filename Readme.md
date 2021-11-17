# Homework Problem

A retailer offers a rewards program to its customers, awarding points based on each recorded
purchase.

A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point
for every dollar spent over $50 in each transaction
(e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).

Given a record of every transaction during a three month period, calculate the reward points
earned for each customer per month and total.

## Technical Notes and Architecture:
+ Code your solution in Java and Spring
+ Make up a data set to best demonstrate your solution
+ Use OOP concepts as much as possible when designing classes.
+ Swagger/OpenAPI (Optional) - helps communicate the contract in a better way
+ Document all response codes expected. The REST API should be sending the
appropriate code and not 200 always.
+ Functional Programming constructs of Java 8 as appropriate.
+ Generic exception handler for un-anticipated exceptions.
+ Assume that the client will look at your commit history, so it should reflect a good project
progression.

## Additional Requirements:
+ Follow standard best practices for structuring the code.
+ Prepare and provide Test Data along with the code.
+ Both positive and negative unit test cases for all operations – to be run as part of the
build process.
+ Implement RestAPI’s for all CRUD operations – in this case – creating/updating
transactions, calculating and providing reward information for a User. (Java Backend/Full
Stack)
+ Consistent error handling and reporting of all failures including unexpected error
conditions
+ Use of appropriate logging levels, framework
+ Reward calculation logic should be accurate
+ Readme file is mandatory – must contain the steps required to build and run/test
the code
+ The solution must be checked into Github (provide a public github url)



## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/km80-cana/retailer-rewards-program.git
```

**2. Create Mysql database**
```bash
create database retailer
```

**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the app using maven**

```bash
mvn package
java -jar target/retailer-1.0.0.jar
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

The app defines following CRUD APIs.

    Get all Purchases    
    GET /api/purchase    

    Get a Purchase by Id;
    GET /api/purchase/{id}

    Create a new Purchase
    POST /api/purchase
    
    Update a Purchase by Id
    PUT /api/purchase/{id}
    
    Delete a Purchase by Id
    DELETE /api/notes/{id}

    Get all Customers Points from the last three months
    GET /api/rewards

You can test them using postman or any other rest client.

