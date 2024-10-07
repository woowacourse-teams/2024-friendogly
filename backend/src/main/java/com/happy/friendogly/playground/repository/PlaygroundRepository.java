package com.happy.friendogly.playground.repository;

import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.playground.domain.Playground;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {

    default Playground getById(Long id) {
        return findById(id).orElseThrow(() -> new FriendoglyException("놀이터 정보를 찾지 못했습니다."));
    }

    default List<Playground> findAllByLatitudeBetweenAndLongitudeBetween(
            double startLatitude,
            double endLatitude,
            double startLongitude,
            double endLongitude
    ) {
        return findAllByLocation_LatitudeBetweenAndLocation_LongitudeBetween(
                startLatitude,
                endLatitude,
                startLongitude,
                endLongitude
        );
    }

    List<Playground> findAllByLocation_LatitudeBetweenAndLocation_LongitudeBetween(
            double startLatitude,
            double endLatitude,
            double startLongitude,
            double endLongitude
    );
}