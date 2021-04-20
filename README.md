# Demo-Forum

This project includes all the modules that requires to bootup the forum project.

The demo-project require the following mircoservice modules

  - API gateway for endpoint security
  - member service for all member related functionalities
  - post service for all post read and write operations

## API doc
For localhost environment: http://localhost:8080/swagger-ui.html

### Tech

This micoservice deploy the following technology and project

* [Spring cloud gateway] - Spring cloud gateway
* [Spring boot] - Service code backend
* [Spring security] - Handle login process and generate JWT token
* [Spring data] - Handle JPA
* [JWT] API authentication and authorisation
* [Docker] Microservice virtualisation
* [Redis] High performance cache service
* [Mariadb] Relational database

### Build

Demo-Forum-Member-Microservice requires gradle v6.5.1+ to run.

```sh
$ gradle build
```
### Run on Docker 
```sh
docker-compose up -d
```

*Please refer to [wiki](https://github.com/gstkenpo/demo-forum/wiki) to check the API details
License
----

MIT


**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [Spring cloud gateway]: <https://spring.io/projects/spring-cloud-gateway>
   [Spring boot]: <https://spring.io/projects/spring-boot>
   [Spring data]: <https://spring.io/projects/spring-data>
   [Spring security]: <https://spring.io/projects/spring-security>
   [JWT]: <https://jwt.io/>
   [Docker]: <https://www.docker.com/>
   [Redis]: <https://redis.io/>
   [Mariadb]: <https://mariadb.org/>