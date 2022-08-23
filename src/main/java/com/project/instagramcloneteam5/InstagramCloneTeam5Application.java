package com.project.instagramcloneteam5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InstagramCloneTeam5Application {

	public static void main(String[] args) {
		SpringApplication.run(InstagramCloneTeam5Application.class, args);
	}

}
