package com.ljf.ploughthewaves;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Configurable
public class PloughTheWavesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PloughTheWavesApplication.class, args);
    }

}
