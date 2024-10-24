package com.happy.friendogly;

import com.happy.friendogly.playground.repository.PlaygroundMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DropPlaygroundMember implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private PlaygroundMemberRepository playgroundMemberRepository;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("deleteAll() 시작");
        playgroundMemberRepository.deleteAll();
        log.info("deleteAll() 완");
    }
}
