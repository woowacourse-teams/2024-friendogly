package com.happy.friendogly.playground.repository;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.playground.domain.Playground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {

    default Playground getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("놀이터 정보를 찾지 못했습니다."));
    }
}
