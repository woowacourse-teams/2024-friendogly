package com.woowacourse.friendogly.footprint.repository;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FootprintRepository extends JpaRepository<Footprint, Long> {

    List<Footprint> findByCreatedAtAfter(LocalDateTime since);
}
