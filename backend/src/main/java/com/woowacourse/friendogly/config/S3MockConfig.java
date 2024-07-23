package com.woowacourse.friendogly.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class S3MockConfig {

    private final S3Mock s3Mock;

    public S3MockConfig() {
        this.s3Mock = new S3Mock.Builder()
                .withPort(8100)
                .withInMemoryBackend()
                .build();
    }

    @Bean
    @Primary
    public AmazonS3 S3Client() {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(
                        new EndpointConfiguration(
                                "http://localhost:8100",
                                Regions.AP_NORTHEAST_2.getName()
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
        amazonS3.createBucket("techcourse-project-2024");
        return amazonS3;
    }

    @PostConstruct
    public void startS3Mock(){
        this.s3Mock.start();
    }

    @PreDestroy
    public void destroyS3Mock(){
        this.s3Mock.shutdown();
    }

}
