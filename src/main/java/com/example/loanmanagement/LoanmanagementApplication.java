package com.example.loanmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// Ensure it scans your entities
@EntityScan("com.example.loanmanagement.entities")
@EnableJpaRepositories("com.example.loanmanagement.repositories")
public class LoanmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanmanagementApplication.class, args);
	}

}
