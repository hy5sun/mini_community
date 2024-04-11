package com.example.mini_community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MiniCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniCommunityApplication.class, args);
	}

}
