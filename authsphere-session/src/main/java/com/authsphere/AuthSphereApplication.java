package com.authsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of the AuthSphere application.
 * This class bootstraps the Spring Boot application,which 
 * 		Starts the embedded server(Tomcat)
 * 		Initializes Spring Application Context,component scanning,auto-configuration 
 * 
 * In this project: 
 *    Spring Security handles authentication & authorization
 *    Session management is backed by Redis (Spring Session)
 *    Supports role-based access (USER / ADMIN)
 * 
 *
 */
@SpringBootApplication
public class AuthSphereApplication {
	//Main method which launches the Spring Boot application.
	public static void main(String[] args) {
		SpringApplication.run(AuthSphereApplication.class, args);
	}

}
