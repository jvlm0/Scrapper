package com.scraping.vagas.Scraping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrapingApplication implements CommandLineRunner {

    @Value("${spring.profiles.active:worker}")
    private String profile;

	public static void main(String[] args) {

        SpringApplication.run(ScrapingApplication.class, args);
	}


    @Override
    public void run(String... args) {
        if ("scheduler".equals(profile)) {
            System.out.println("🚀 Rodando como Scheduler");
        } else {
            System.out.println("🧩 Rodando como Worker");
        }
    }

}
