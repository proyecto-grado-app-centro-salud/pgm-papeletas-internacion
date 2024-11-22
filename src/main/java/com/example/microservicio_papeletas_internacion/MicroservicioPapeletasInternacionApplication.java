package com.example.microservicio_papeletas_internacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@SpringBootApplication
public class MicroservicioPapeletasInternacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioPapeletasInternacionApplication.class, args);
	}

}
