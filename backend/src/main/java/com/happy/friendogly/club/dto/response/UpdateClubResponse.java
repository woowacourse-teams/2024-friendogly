package com.happy.friendogly.club.dto.response;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.domain.Status;

public record UpdateClubResponse(
        String title,
        String content,
        Status status
) {

    public UpdateClubResponse(Club club) {
        this(
                club.getTitle().getValue(),
                club.getContent().getValue(),
                club.getStatus()
        );
    }
}
