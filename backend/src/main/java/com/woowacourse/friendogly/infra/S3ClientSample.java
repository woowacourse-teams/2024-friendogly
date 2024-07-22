package com.woowacourse.friendogly.infra;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Component
public class S3ClientSample {
    private final S3Client s3Client;

    S3ClientSample(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    void readFile() throws IOException {
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                request -> request.bucket("d3obq7hxojfffa.cloudfront.net").key("test_dog.jpg"));

        String fileContent = StreamUtils.copyToString(response, StandardCharsets.UTF_8);

        System.out.println(fileContent);
    }
}
