package com.happy.friendogly.playground.service;

import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
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

    public SavePlaygroundResponse save(SavePlaygroundRequest request, Long memberId) {
        Playground savedPlayground = playgroundRepository.save(
                new Playground(new Location(request.latitude(), request.longitude()))
        );
        
        return SavePlaygroundResponse.from(savedPlayground);
    }
}
