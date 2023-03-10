package com.pentspace.accountmgtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountMgtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountMgtServiceApplication.class, args);
	}

}
