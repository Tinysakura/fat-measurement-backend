package com.cfh.fatmeasurementsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FatMeasurementsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FatMeasurementsBackendApplication.class, args);
    }

}
