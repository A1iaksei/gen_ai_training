package com.epam.training.gen.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * SpringBoot Application main class of the Gen-AI training project.
 */
@SpringBootApplication
@PropertySource("classpath:/config/application.properties")
public class GenAiTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenAiTrainingApplication.class, args);
	}

}
