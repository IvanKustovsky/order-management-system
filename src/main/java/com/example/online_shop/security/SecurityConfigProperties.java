package com.example.online_shop.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityConfigProperties {

    private List<String> allowedRoutes;
}
