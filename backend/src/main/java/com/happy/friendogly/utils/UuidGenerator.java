package com.happy.friendogly.utils;

import java.util.UUID;

public class UuidGenerator {

    // TODO: 정말 Unique한 UUID를 생성하는 로직으로 갈아끼우기
    public static String generateUuid() {
        return UUID.randomUUID().toString()
                .substring(0, 8);
    }
}
