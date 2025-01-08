package com.happy.friendogly.config;

import static com.happy.friendogly.common.ErrorCode.FAILED_LOAD_SERVER_FIREBASE_CREDENTIALS;
import static com.happy.friendogly.common.ErrorCode.INVALID_FIREBASE_CREDENTIALS;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.happy.friendogly.exception.FriendoglyException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

@Configuration
@Profile("!local")
public class FirebaseAppConfig {

    @Value("${file.firebase.path}")
    private String FIREBASE_FILE_PATH;

    @Bean
    FirebaseApp firebaseApp() {
        InputStream firebaseCredentialsInputStream = getFirebaseCredentialsInputStream();

        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseCredentialsInputStream))
                    .build();
        } catch (IOException e) {
            throw new FriendoglyException(
                    "서버에서 Firebase인증을 실패했습니다.",
                    INVALID_FIREBASE_CREDENTIALS,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

    private InputStream getFirebaseCredentialsInputStream() {
        Resource resource = new ClassPathResource(FIREBASE_FILE_PATH);
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new FriendoglyException(
                    "서버에서 파이어베이스 인증정보 가져오기를 실패했습니다.",
                    FAILED_LOAD_SERVER_FIREBASE_CREDENTIALS,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(){
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
