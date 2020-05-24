# Springboot Oauth2 playground
In this repo I am creating very simple Springboot projects to learn a more about Oauth2 and how I can use it.

**Requirements**

- Java 11
- Maven 3.x

## Web App client
This project provides a simple UI for the browser. The user can navigate to [home page](http://localhost:8080/webapp) and decide how to sign in.

The login screen has a simple form, if the user will use this form, he will be authenticated against the "security" logic implemented in the Wev App Client service. Otherwise he can decide to login via the Authorisation Service using Oauth2 and the "authorisation code" grant type.

![Diagram](./misc/img/login.png)

## Authorisation Service
This service is a Springboot project which allows the user to authorise the access to the protected resource (Resource service) to a third party client service (Webapp Client service). This service uses the Oauth2 protocol, and generates JWT tokens.

## Resource Service

This service provides a protected API implementing the Oauth2 protocol. The client wants to access the protected API needs first to retrieve a JWT token from the authorisation service.

## Workflow
![Diagram](./misc/img/diagram.png)
1. The Resource Owner (user) open the browser and opens the [home page](http://localhost:8080/webapp) of the Webapp
1. The reousrce owner selects the log-in via the authorisation service, the Webbapp redirects the resource owner (user) to the Authorisation service
1. The user logs in as "admin" user 
1. The user authorise the "Webapp" to access his resources stored on the resource server
1. The authorisation service redirect the user to the Webapp client and providing as parameter the "webapp" access code
1. The Webbapp receives the access code, and uses this code to retrieve the authorisation token from the Authorisation service
1. The Webbapp receives the Authorisation token, makes a call to the Resource service using the authorisation code
1. The Resource Service validates the recived Authorisation token using the Authorisation Service public sign key
1. The Resource Service sends the reply to the Webapp client 
1. The Webbapp displays the Resource reply to the Resource owner (user)

### TODO
 - Customise the JWT token and add some extra claims
 - Read the customised JWT token from the Webapp service
