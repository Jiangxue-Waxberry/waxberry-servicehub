package com.jiangxue.waxberry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WaxberryManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaxberryManagerApplication.class, args);
	}

}
