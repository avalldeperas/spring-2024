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
    EPCSD project stub
    <br />
    <br />
    <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/issues">Report Bug</a>
    ·
    <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Contents</summary>
  <ol>
    <li>
      <a href="#about-this-project">About this project</a>
      <ul>
        <li><a href="#made-with">Made with</a></li>
      </ul>
    </li>
    <li>
      <a href="#before-starting">Before starting</a>
    </li>
    <li>
      <a href="#installation">Tnstallation</a>
      <ul>
        <li><a href="#docker-desktop--docker-compose-installation">Docker Desktop / Docker Compose installation</a></li>
        <li><a href="#basic-infrastructure-dockers">Basic infrastructure (dockers)</a></li>
        <li><a href="#microservices-stubs">Microservices stubs</a></li>
      </ul>
    </li>
    <li><a href="#links-to-tools-libraries-and-used-modules">Links to tools, libraries and used modules</a></li>
    <li><a href="#additional-considerations">Additional considerations</a></li>
  </ol>
</details>

<!-- About this project -->
## About this project

This is the lab project for the EPCSD course at the UOC. It is made up of 3 elements (each one in its own GIT repository):

* A <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/blob/main/docker-compose.yml">docker-compose.yml</a> file to startup the basic infrastructure needed to run the services
* A folder for the <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024-productcatalog">ProductCatalog</a> microservice 
* A folder for the <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024-user">User</a> microservice 
* A folder for the <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024-notification">Notification</a> microservice 

<p align="right">(<a href="#top">go up</a>)</p>


### Made with

* [Docker](https://www.docker.com/) / [Docker Compose](https://github.com/docker/compose)
* [Spring](https://spring.io/) / [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Apache Kafka](https://kafka.apache.org/)
* [PostgreSQL](https://www.postgresql.org/)

<p align="right">(<a href="#top">go up</a>)</p>


## Before starting

To set up the containers that are part of the basic infrastructure of the project, the following ports will be used:

* 22181 - Apache Kafka (Zookeeper)
* 19092 - Apache Kafka (Server)
* 54320, 54321 - PostgreSQL
* 18080 - Adminer
* 18081 - Used by the productcatalog microservice
* 18082 - Used by the user microservice

To avoid conflicts with other installed applications, the default ports of all applications have been modified. Still, if there is a conflict over a port already in use, simply modifying the ports specified in the [docker-compose.yml](https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/blob/main/docker-compose.yml) file will fix the problem. This link to the official docker compose documentation explains how to modify this configuration using the _ports_: [Networking in Compose](https://docs.docker.com/compose/networking/) option.

__IMPORTANT NOTICE:__ The modified ports will also have to be changed in the microservices configuration (usually defined in the Spring _application.properties_ file).


## Installation

### Docker Desktop / Docker Compose installation

Proceed to install Docker Compose following the steps described in the following guide: https://docs.docker.com/compose/install/ (according to your OS).

Under Windows, registration may be required, as <a href="https://docs.docker.com/desktop/windows/install/">Docker Desktop</a>  requires it for educational/personal/non-commercial projects. On the plus side, it will not be necessary to install anything else because it already includes _Compose_.

It is important that you carefully review the hardware and software requirements described in the installation guides. If your system fails to meet them, even after a successful installation, you will see errors when trying to start containers. An alternative for those with slightly older systems is <a href="https://www.how2shout.com/how-to/how-to-install-docker-toolbox-using-chocolatey-choco-on-windows-10.html">Docker Toolbox</a>.

Once Docker Compose is installed, we will continue with the project stub. It is recommended to set up a folder structure like so:

```
spring-2024
├ README.md
├ docker-compose.yml
├ spring-2024-notification
├ spring-2024-productcatalog
└ spring-2024-user
```

<p align="right">(<a href="#top">go up</a>)</p>


### Basic infrastructure (dockers)

* Download the code in ZIP format or just clone the <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024">spring-2024</a> repository in the working folder (_spring-2024_ if the recommendation has been followed).

* From the work folder, run the command:

  ```sh
  docker compose up
  (Win)
  ```
  ```sh
  docker-compose up
  (Linux)
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
  
We should see something like this:

![Docker containers running](https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/blob/main/docker__containers_running.PNG)

To check the operation, you can access the _Adminer_ panel at http://localhost:18080/ and make a query against the PostgreSQL DB that we have just instantiated with the following connection data:

* productdb
* Engine: PostgreSQL
* Server: productdb
* User: product
* Password: product
* Schema: product

![Adminer productdb 1](https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/blob/main/adminer1.PNG)

![Adminer productdb 2](https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/blob/main/adminer1_1.PNG)

* userdb
* Engine: PostgreSQL
* Server: userdb
* User: user
* Password: user
* Schema: user

![Adminer userdb 1](https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/blob/main/adminer2.PNG)

![Adminer userdb 2](https://github.com/UOC-EPCSD-SPRING-2024/spring-2024/blob/main/adminer2_1.PNG)


### Microservices stubs

* Download the code in ZIP format or just clone the <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024-productcatalog">spring-2024-productcatalog</a>, <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024-user">spring-2024-user</a> and <a href="https://github.com/UOC-EPCSD-SPRING-2024/spring-2024-notification">spring-2024-notification</a> repositories into the working folder (_spring-2024_ if the recommendation has been followed)
* Open the projects in the preferred development environment
* Verify proper build and run by starting the projects and checking that http://localhost:18081/swagger-ui/index.html and http://localhost:18082/swagger-ui/index.html are accessible

<p align="right">(<a href="#top">go up</a>)</p>


## Links to used tools, libraries and modules

* [Docker](https://www.docker.com/) / [Docker Compose](https://github.com/docker/compose)
* [Spring](https://spring.io/) / [Spring Boot](https://spring.io/projects/spring-boot)
  * [spring-data-jpa](https://spring.io/projects/spring-data-jpa)
  * [spring-data-jdbc](https://spring.io/projects/spring-data-jdbc)
  * [spring-kafka](https://spring.io/projects/spring-kafka)
* [Apache Kafka](https://kafka.apache.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [Lombok](https://projectlombok.org/)
* [springdoc-openapi-ui (SwaggerUI for OpenApi 3)](https://github.com/springdoc/springdoc-openapi)


## Additional Considerations
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

<p align="right">(<a href="#top">go up</a>)</p>


