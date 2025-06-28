package com.jiangxue.waxberry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WaxberryFileServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaxberryFileServerApplication.class, args);
	}

}
