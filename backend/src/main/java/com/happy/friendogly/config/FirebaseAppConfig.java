package com.happy.friendogly.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class FirebaseAppConfig {

    @Value("${file.firebase.path}")
    private String FIREBASE_FILE_PATH;

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        Resource resource = new ClassPathResource(FIREBASE_FILE_PATH);
        InputStream credentialsStream = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build();

        if(FirebaseApp.getApps().isEmpty()){
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

}
