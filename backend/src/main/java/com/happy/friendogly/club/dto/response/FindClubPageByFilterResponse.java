package com.happy.friendogly.club.dto.response;

import java.util.List;

public record FindClubPageByFilterResponse(boolean isLastPage, List<FindClubByFilterResponse> content) {

}
