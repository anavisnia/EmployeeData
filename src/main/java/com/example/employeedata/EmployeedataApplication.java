package com.example.employeedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories("com.example.employeedata.repository.elastic")
public class EmployeedataApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeedataApplication.class, args);
	}

}
