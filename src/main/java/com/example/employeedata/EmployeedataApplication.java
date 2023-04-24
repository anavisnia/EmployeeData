package com.example.employeedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableElasticsearchRepositories("com.example.employeedata.repository.elastic")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class EmployeedataApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeedataApplication.class, args);
	}

}
