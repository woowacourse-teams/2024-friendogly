package com.happy.friendogly.playground.service;

import com.happy.friendogly.playground.repository.PlaygroundMemberRepository;
import com.happy.friendogly.playground.repository.PlaygroundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaygroundCommandService {

    private final PlaygroundRepository playgroundRepository;
    private final PlaygroundMemberRepository playgroundMemberRepository;

    public PlaygroundCommandService(
            PlaygroundRepository playgroundRepository,
            PlaygroundMemberRepository playgroundMemberRepository
    ) {
        this.playgroundRepository = playgroundRepository;
        this.playgroundMemberRepository = playgroundMemberRepository;
    }
}
