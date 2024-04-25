package ru.becoder.krax;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan
@EntityScan
@EnableJpaRepositories
@Log4j2
public class KraxApplication {
	public static void main(String[] args) {
		log.error("ERROR");
		log.info("INFO");
		SpringApplication.run(KraxApplication.class, args);
	}

}
