# IP6_23FS_IMVS33_Cyberduck_goes_Enterprise_mk

## Description
Setup is according to the minIO manual: 
https://min.io/docs/minio/linux/operations/external-iam/configure-keycloak-identity-management.html#minio-authenticate-using-keycloak


## Installation
Install and start Docker Desktop  : https://docs.docker.com/desktop/

## Usage
- Start the container and the predetermined configuration with *docker-compose.yml* in the project.
- Login information is stored in *docker-compose.yml* and should be changed there.
- Start the prototype in cyberduck_oidc_prototype\prototype\src\main\java\ch\oidc\Main.java
- Click the link for the authorization code and copy the code from the browser url
- Enter the code in the terminal
- Enter a char to start the upload of the test.txt file
- The file should be uploaded in the bucket

### MiniO
The installation and configuration of keycloak will be performed automatically with the start of *docker-compose.yml*

You can check the functionality by the following curl requests (according to the MinIO manual):
- curl -d "client_id=minio" -d "client_secret=password" -d "grant_type=password" -d "username=sherom" -d "password=password" http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token
- curl -X POST "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token" -H "Content-Type: application/x-www-form-urlencoded" -d "username=sherom" -d "password=password" -d "grant_type=password" -d "client_id=minio" -d "client_secret=password"
- curl -X POST "http://localhost:9000" -H "Content-Type: application/x-www-form-urlencoded" -d "Action=AssumeRoleWithWebIdentity" -d "Version=2011-06-15" -d "DurationSeconds=86000" -d "WebIdentityToken=TOKEN"

### Keycloak
The installation and configuration of keycloak will be performed automatically with the start of *docker-compose.yml*

#### Additional configuration:
A new client user with his password has to be configured in Keycloak. This is the only configuration that at the time of 
writing this description can't be automated.

## Project status
This Project is in development and will be used as an example for further implementation.