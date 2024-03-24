package org.example.marketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.marketserver.repositories")
public class MarketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketServerApplication.class, args);
	}
}
