## Here are the steps to generate the Client ID, Client Secret, and JWT Secret for the application.

Using Command Line (Linux/macOS):
```bash
# Generate Client ID (24 bytes)
CLIENT_ID=$(openssl rand -base64 18 | tr -d /=+ | cut -c -24)
echo "Client ID: $CLIENT_ID"

# Generate Client Secret (48 bytes)
CLIENT_SECRET=$(openssl rand -base64 36 | tr -d /=+ | cut -c -48)
echo "Client Secret: $CLIENT_SECRET"

# Generate JWT Secret (64 bytes)
JWT_SECRET=$(openssl rand -base64 48 | tr -d /=+ | cut -c -64)
echo "JWT Secret: $JWT_SECRET"
```

Add values to application.properties file:
```properties sample
spring.security.client.id=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
spring.security.client.secret=Zm9vYmFyMjM1Pz8pX19lbmNyeXB0
spring.security.jwt.secret=aGVsbG9zdGFrZXRoZXNlY3JldGZvcnlvdXJhcHBsaWNhdGlvbg# BankingService
```

# Running the Application with Docker
## Running the Docker Containers

Docker files are added to /dockerfiles directory. The docker-compose.yml file is used to define and run multi-container Docker applications.

Build the Docker Image: Run the following command to build your Docker image for the banking service:

`docker-compose build`

## Start the Containers: Start your containers using the following command:
`docker-compose up`