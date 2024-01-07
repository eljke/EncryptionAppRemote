package ru.ystu.encryptionapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class EncryptionApp {
	@Value("${app.hostingAddress}")
	private String hostingAddress;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EncryptionApp.class, args);
		EncryptionApp app = context.getBean(EncryptionApp.class);
		log.info("App started on address: {}", app.hostingAddress);
	}

}
