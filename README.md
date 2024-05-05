<div id="top"></div>
<!--
*** Made using the Best-README-Template
*** https://github.com/othneildrew/Best-README-Template/blob/master/README.md
-->


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <h3 align="center">EPCSD</h3>

  <p align="center">
    EPCSD PRAC2 - Photo&Film4You
  </p>
</div>


## About this project
This is the PRAC2 project for the EPCSD course at the UOC. It is made up of 3 elements (each one in its own GIT repository):

* A <a href="https://github.com/avalldeperas/spring-2024/blob/main/docker-compose.yml">docker-compose.yml</a> file to startup the basic infrastructure needed to run the services
* A folder for the <a href="https://github.com/avalldeperas/spring-2024/tree/main/spring-2024-productcatalog">ProductCatalog</a> microservice 
* A folder for the <a href="https://github.com/avalldeperas/spring-2024/tree/main/spring-2024-user">User</a> microservice 
* A folder for the <a href="https://github.com/avalldeperas/spring-2024/tree/main/spring-2024-notification">Notification</a> microservice 

Followed the recommended folder structure like so:

```
spring-2024
├ README.md
├ docker-compose.yml
├ spring-2024-notification
├ spring-2024-productcatalog
└ spring-2024-user
```


## Before starting
To set up the containers that are part of the basic infrastructure of the project, the following ports will be used:

* 22181 - Apache Kafka (Zookeeper)
* 19092 - Apache Kafka (Server)
* 54320, 54321 - PostgreSQL
* 18080 - Adminer
* 18081 - Used by the productcatalog microservice
* 18082 - Used by the user microservice


## Steps to run the code

### Download the code

* Download the code in ZIP format or just clone the <a href="https://github.com/avalldeperas/spring-2024/tree/main/spring-2024-user">spring-2024</a> repository in the working folder.

### Start docker
* From the work folder, run the command:
  ```sh
  docker compose up
  (Win)
  ```
  
The following containers should start:

* spring-2024-adminer_1 - adminer, an SQL client
* spring-2024-kafka_1 - the kafka server
* spring-2024-productdb_1 - the postgresql database for the productcatalog service
* spring-2024-userdb_1 - the postgresql database for the user service
* spring-2024-zookeeper_1 - kafka zookeeper

In order to verify that all containers are up and running, we will execute the following command:

  ```sh
  docker ps -a
  ```

#### Adminer panel
To check the operation, you can access the _Adminer_ panel at http://localhost:18080/ and make a
query against the PostgreSQL DB that we have just instantiated with the following connection data:

Product database:
```
* productdb
* Engine: PostgreSQL
* Server: productdb
* User: product
* Password: product
* Schema: product
```

User database:
```
* userdb
* Engine: PostgreSQL
* Server: userdb
* User: user
* Password: user
* Schema: user
```


### Run microservices:

1. Run Product catalog microservice with the command below:
```
java -jar productcatalog-0.0.1-SNAPSHOT.jar
```

2. Run User microservice with the command below:
```
java -jar user-0.0.1-SNAPSHOT.jar
```

3. Run Notification microservice with the command below:
```
java -jar notification-0.0.1-SNAPSHOT.jar
```

4. Verify access to swagger:
  * http://localhost:18081/swagger-ui/index.html (Product catalog)
  * http://localhost:18082/swagger-ui/index.html (User)

5. Download and run the postman collection added to the zip file.


## Additional Notes and Considerations

### Product Catalog Microservice
#### Products
* _createProduct_: Added additional check that product does not exist in catalog with same name as requested in PRAC1 solution.
* _deleteProduct_: Could not check that unit is compromised in any current or future rent as requested in PRAC1,
because rent service is not implemented in PRAC2.
* _findProductsByCriteria_: a single endpoint has been exposed to query by all criteria. Also added brand, model and description. 

#### Category
* _findCategoriesByCriteria_: similarly as _findProductsByCriteria_, a single endpoint has been created to be able to
query Categories by different criteria.

#### Items
* _createItem_: As requested in PRAC1 solution, an additional check has been added to ensure that there aren't any items with 
same serial in database already.
* _updateItem_: New endpoint to update item is exposed to both statuses (OPERATIONAL/NON_OPERATIONAL) to a given item
by its serial number. As requested in PRAC1 solution, an additional check has been added to ensure that product exists 
with oposite status than the one added in the request (to avoid setting the same status again).

#### Offer
A new entity, service, repository and controller have been added as part of PRAC2.
As requested in PRAC1 solution:
* _createOffer_: to ensure that user exists in the system, a rest request has been added to call 
user microservice (similarly as notification service). Also, ensured that product and category exist in the system. 
Finally, a PENDING status has been added when users create offers by default, pending to be evaluated by administrators.
* _findOffersByUser_: has decided to not add a check that user exists as it will simply return an empty list.
* _evaluateOffer_: only verifies that offer exists in database beforehand, like PRAC1 contract requested. When admin  
accepts user offer, a new item is created while reusing item service logic. Therefore, a new event is published to the
kafka topic and only item service is responsible for that. Finally, to not break PRAC1 contract, an offer can be evaluated
as many times as admin requests.

### User Microservice
#### User
- _createUser_: as requested in PRAC1 solution, a validation has been added to check that email does not exist in database
before creating a user.
- _getUsersToAlert_: as discussed in the forum, although this operation uses _findAlerts**ByProductIdAndInterval**_, 
dates are not a interval but more of a specific date.

#### Alert
- _findAlerts**ByProductIdAndDate**_: query alerts by product and a specific date, not an interval as discussed in the 
subject forum. This operation reuses same _findAlerts**ByProductIdAndInterval**_ than used in getUsersToAlert operation.
- _findAlerts**ByUserIdAndInterval**_: query alerts by a given user and an interval of dates (from and to dates). This
is the only operation that really uses an interval of dates. 

### Notification Microservice
Used RestTemplate with the userServiceUrl to query the User microservice in order to get the users having an alert for
the specified product and the actual date (LocalDate.now). 
Email has been simulated as requested in the exercise with a log like below:

`NotificationService     : Sending an email to user 'Nil Carbonell'... `


