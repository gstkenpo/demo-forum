# Demo-Forum-Member-Microservice

This project includes all the member related function for [demo-forum](https://www.google.com). The microservice is designed to run on secure environment instead of public cloud or any sort.

The member microservice would provide the following service.

  - Creation and editing member
  - Group of admin functions (query/ edit/ delete member info)
  - Generaten and renew JWT for login credential
  - fire an request to update other module on the JWT expiry date

#### How to test
1. Call login API and get JWT with curl command
    ```sh
    curl -i -H "Content-Type: application/json" -X POST -d '{
        "email": "xxx@yyy.com",
        "password": "abcd1234"
    }' http://localhost:8080/rest/login
    ```
2. Copy the JWT to autherisation tab
eg: Bearer xxx 