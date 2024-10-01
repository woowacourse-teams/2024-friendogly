package com.happy.friendogly.playground.service;

import com.happy.friendogly.playground.repository.PlaygroundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaygroundQueryService {

    private final PlaygroundRepository playgroundRepository;

    public PlaygroundQueryService(PlaygroundRepository playgroundRepository) {
        this.playgroundRepository = playgroundRepository;
    }
}
