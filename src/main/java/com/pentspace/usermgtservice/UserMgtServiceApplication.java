package com.pentspace.usermgtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserMgtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMgtServiceApplication.class, args);
	}

}
