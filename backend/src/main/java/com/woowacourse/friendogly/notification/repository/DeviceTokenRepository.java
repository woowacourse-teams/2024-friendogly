package com.woowacourse.friendogly.notification.repository;

import com.woowacourse.friendogly.notification.domain.DeviceToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByMemberId(Long memberId);
}
