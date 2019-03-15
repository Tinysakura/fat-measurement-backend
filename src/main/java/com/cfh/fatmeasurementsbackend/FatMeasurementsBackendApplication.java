package com.cfh.fatmeasurementsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ImportResource(locations = { "classpath:conf/applicationContext.xml" })
public class FatMeasurementsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FatMeasurementsBackendApplication.class, args);
    }

}
