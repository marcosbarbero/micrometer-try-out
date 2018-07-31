package com.marcosbarbero.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", appName);
    }

    @RestController
    class SampleController {

        @GetMapping("/api/sample")
        public String sample() throws InterruptedException {
            longProcess();
            return UUID.randomUUID().toString();
        }

        private void longProcess() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep((long) new Random().nextInt(300));
        }

    }
}
