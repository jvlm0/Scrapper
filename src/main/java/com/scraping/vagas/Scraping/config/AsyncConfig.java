package com.scraping.vagas.Scraping.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "workerExecutor")
    public Executor workerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);   // threads mínimas
        executor.setMaxPoolSize(20);    // threads máximas
        executor.setQueueCapacity(50);  // fila de espera
        executor.setThreadNamePrefix("WorkerAsync-");
        executor.initialize();
        return executor;
    }
}
