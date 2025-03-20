package com.example.online_shop;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Order Management System REST API Documentation",
				description = "This API allows you to manage orders, including creation, retrieval, and cancellation.",
				version = "v1",
				contact = @Contact(
						name = "Ivan Kustovskyi",
						email = "vanyakustovsky@gmail.com",
						url = "https://example.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://t.me/micromolekula11_00"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Order Management System API Documentation",
				url = "https://www.project-domain/swagger-ui/index.html"
		)
)
public class OnlineShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

}
