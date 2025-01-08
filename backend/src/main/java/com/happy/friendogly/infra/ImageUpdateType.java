package com.happy.friendogly.infra;

import com.happy.friendogly.exception.FriendoglyException;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum ImageUpdateType {

    UPDATE,
    NOT_UPDATE,
    DELETE;

    public static ImageUpdateType from(String rawImageUpdateType) {
        try {
            return valueOf(rawImageUpdateType);
        } catch (IllegalArgumentException e) {
            throw new FriendoglyException(
                    String.format("존재하지 않는 ImageUpdateType 입니다. %s 중 하나로 입력해주세요.", createAllowedValues())
            );
        }
    }

    private static String createAllowedValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
