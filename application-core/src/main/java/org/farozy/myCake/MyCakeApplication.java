package org.farozy.myCake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.farozy.entity")
@EnableJpaRepositories(basePackages = "org.farozy.repository")
@ComponentScan(basePackages = {"org.farozy"})
public class MyCakeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyCakeApplication.class, args);
	}

}
