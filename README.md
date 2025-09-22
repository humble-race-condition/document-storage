# DocumentStorage
A service for storing documents that implements the idea of the SAGA design pattern.

The problem arises when a transaction stores a record for a file, but the binary might not have been uploaded,
or when the binary is uploaded, but the transaction does not finish. In these scenarios, the state of the
application is not valid. For this a Transaction Action Records are implemented, that are always persisted
before the business logic of the endpoint call begins. This way, if an error occurs, there is a marker for the
state of the file.

The project provides a simple REST API containing multiple solutions to everyday problems.
A dynamic register with filters, structured rest api endpoints, localized error messages with codes,
implemented problem details, generalized error handling, unit and integration tests, Dockerfile,
Jenkinsfile, docker-compose with .env and many others.

### Run the application
To run the application, copy the following files from the repository:  
`./infrastructure/.env`  
`./infrastructure/docker-compose.yml`  
`./infrastructure/db/init.sql`  

files in a selected folder on a Linux machine. In that selected folder run:  
`docker compose up -d`

Postman collections can be found in `./documentation` folder.

Swagger is also provided where port is `port` is set from the `.env` file. 
The default port for the `main` branch is `40800`.  
http://host:40800/swagger-ui/index.html
