package com.happy.friendogly.playground.repository;

import com.happy.friendogly.playground.domain.PlaygroundMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundMemberRepository extends JpaRepository<PlaygroundMember, Long> {

}
