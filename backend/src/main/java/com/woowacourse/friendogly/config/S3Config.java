package com.woowacourse.friendogly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client S3Client(){
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }
}
