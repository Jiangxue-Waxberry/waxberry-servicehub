package com.jiangxue.waxberry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WaxberryAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaxberryAuthApplication.class, args);
	}

}
