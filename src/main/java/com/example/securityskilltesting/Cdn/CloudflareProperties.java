package com.example.securityskilltesting.Cdn;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="cloudflare.r2")
public class CloudflareProperties {
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String endpoint;
}
