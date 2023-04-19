# IP6_23FS_IMVS33_Cyberduck_goes_Enterprise_mk

## Description
Setup is according to the minIO manual: 
https://min.io/docs/minio/linux/operations/external-iam/configure-keycloak-identity-management.html#minio-authenticate-using-keycloak

you can check the functionality by the following curl requests (according to the MinIO manual):

curl -d "client_id=minio" -d "client_secret=password" -d "grant_type=password" -d "username=sherom" -d "password=sherom" http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token

curl -X POST "http://localhost:8080/auth/realms/cyberduckrealm/protocol/openid-connect/token" -H "Content-Type: application/x-www-form-urlencoded" -d "username=sherom" -d "password=sherom" -d "grant_type=password" -d "client_id=minio" -d "client_secret=password"

curl -X POST "http://localhost:9000" -H "Content-Type: application/x-www-form-urlencoded" -d "Action=AssumeRoleWithWebIdentity" -d "Version=2011-06-15" -d "DurationSeconds=86000" -d "WebIdentityToken=TOKEN"

## Installation


## Usage

### MiniO

### Keycloak

## License
For open source projects, say how it is licensed.

## Project status
This Project is in development and will be used as an example for further implementation.