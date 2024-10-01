package com.happy.friendogly.playground.service;

import com.happy.friendogly.playground.repository.PlaygroundRepository;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundCommandService {

    private final PlaygroundRepository playgroundRepository;

    public PlaygroundCommandService(PlaygroundRepository playgroundRepository) {
        this.playgroundRepository = playgroundRepository;
    }
}
