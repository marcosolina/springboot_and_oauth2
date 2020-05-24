# Springboot Oauth2 playground
In this repo I am creating a very simple Springboot projects to learn more about Oauth2 and how I can use it.

**Requirements**

- Java 11
- Maven 3.x

## Web App client
This project provides a simple UI for the browser. The user can navigate to [home page](http://localhost:8080/webapp) and decide how to sign in. The login screen has a simple form, if the user will use this form, he will be authenticated against the "security" logic implemented in the Webapp Client service. Otherwise he can decide to login via the Authorisation Service using Oauth2 and the grant type "authorisation_code".

![Diagram](./misc/img/login.png)

## Authorisation Service
This service is a Springboot project which allows the user to grant the access to the protected resource (Resource service) to a third party service (Webapp Client service). This service uses the Oauth2 protocol, and generates JWT tokens.

## Resource Service

This service provides a protected API secured using the Oauth2 protocol. If a client wants to access the protected API he needs first to retrieve a JWT token from the Authorisation service.

## Workflow
![Diagram](./misc/img/diagram.png)
1. The Resource Owner (user) opens the browser and opens the [home page](http://localhost:8080/webapp) of the Webapp
1. The reousrce owner selects the log-in via the authorisation service, the Webbapp redirects the resource owner (user) to the Authorisation service
1. The user logs in as "admin" user 
1. The user authorise the "Webapp" to access his resources stored on the resource server
1. The authorisation service redirect the user to the Webapp client and provides as parameter to the "webapp" the access code
1. The Webbapp receives the access code, and uses this code to retrieve the authorisation token from the Authorisation service
1. The Webbapp receives the Authorisation token and it makes a call to the Resource service using the authorisation code
1. The Resource Service validates the received Authorisation token using the Authorisation Service public sign key
1. The Resource Service sends the reply to the Webapp client 
1. The Webbapp displays the Resource service reply to the Resource owner (user)

### TODO
 - Customise the JWT token and add some extra claims
 - Read the customised JWT token from the Webapp service
