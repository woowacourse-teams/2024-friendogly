package com.happy.friendogly.playground.repository;

import com.happy.friendogly.playground.domain.Playground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {

}
