package com.luthfihariz.esbasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EsbasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsbasicApplication.class, args);
	}

}
