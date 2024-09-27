package com.lcwd.electronicstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication

//below annotation is for swagger openAPI
@EnableWebMvc
//@EntityScan("com.lcwd.electronicstore.entities")
public class ElectronicshopApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ElectronicshopApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("abcd"));
	}
	
	
	//Springfox does not support spring boot 3 so we will use openApi for swwager
	//Reference this https://springdoc.org/migrating-from-springfox.html
	//https://youtu.be/UvIWQSKz8kE?si=rVOclVZI2OED4LIv   durgesh
	//http://localhost:8080/swagger-ui/index.html
	
}
