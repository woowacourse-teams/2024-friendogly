package com.woowacourse.friendogly.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseAppConfig {

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        FileInputStream credentialsStream = new FileInputStream(
                "src/main/resources/firebase-friendogly-private-key.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build();

        if(FirebaseApp.getApps().isEmpty()){
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

}
