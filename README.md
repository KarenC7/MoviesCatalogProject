**MAVEN RUN**  
- mvn clean install    <- Clean and run test
- mvn spring-boot:run  <- Run application

**API DOCUMENTATION SWAGGER**
http://localhost:8080/swagger-ui/index.html#/

**POSTMAN COLLECTION AND MYSQL DATABASE**
- Postman Collection Path: movies\src\main\resources\MoviesCatalogAPIKarenCastillo.postman_collection
- MySQL database Path: movies\src\main\resources\movies_catalog.sql
- DB User: Root
- DB Password: Root


**Movies Catalog Description**

API to manage a movie catalog. 
The app is writed with Spring Boot 2.7.X and Java 11 using a connection to a MYSQL database  

**Data Structure:** 
- Movies have general properties like name, release year, and movie synopsis. 
- Movies can have a image poster
- Movies are categorized (science fiction, comedy ...)
- Movies can have a relation with user who created the movie
- Created Date 

**Users:** 
- Multiple users can access the system
- Users use their emails to authenticate
- Users can rate movies
- Users can be assigned 1 of 2 roles: admin or user

**General:**
- API should have swagger UI implemented

**Movies:**
- Endpoints where admins can manage movies (add, update, delete). Only admin users can access this endpoint.
- Create an endpoint to return movies. The endpoint should be:
*Searchable by name or synopsis content.*
*Filterable by category, year of release.*
*Paginated, specify how many movies you want per page.*
*Orderable by year, name, created date or rating.*

**Authentication:**
- Endpoint to authenticate the user. Provide his user and password if valid; you will return a JWt token.
- Endpoint to register new users.

**Ratings:**
- Endpoint to rate movies; only authenticated users can rate movies. Movies can be rated only once per user.
- Endpoint to remove a movie rating for the authenticated user.
- Endpoint to return all movies ratings for the authenticated user

**Additional:**
- Integration tests for the endpoints.
- Allow uploading a movie image.
- Cache the response of the list movie endpoint



