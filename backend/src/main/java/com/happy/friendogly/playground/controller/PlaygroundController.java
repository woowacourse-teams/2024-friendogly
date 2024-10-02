package com.happy.friendogly.playground.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.request.UpdatePlaygroundArrivalRequest;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundSummaryResponse;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.playground.dto.response.UpdatePlaygroundArrivalResponse;
import com.happy.friendogly.playground.dto.response.detail.PlaygroundPetDetail;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playgrounds")
public class PlaygroundController {

    public static Pet dummyPet1 = new Pet(
            new Member("김도선", "tag1", "imgaeUrl"),
            "초코",
            "뛰어놀기 좋아해요",
            LocalDate.of(2024, 9, 27),
            SizeType.LARGE,
            Gender.FEMALE,
            "https://i.pinimg.com/564x/d1/62/cb/d162cb12dfa0011a7bd67188a14d661c.jpg"
    );
    public static Pet dummyPet2 = new Pet(
            new Member("김도선", "tag1", "imageUrl"),
            "치키",
            "가만히 있길 좋아해요",
            LocalDate.of(2024, 9, 25),
            SizeType.MEDIUM,
            Gender.FEMALE,
            "https://i.pinimg.com/564x/96/d4/43/96d443c92059f2b3a240a7ff74692bbf.jpg"
    );

    public static Pet dummyPet3 = new Pet(
            new Member("박예찬", "tag2", "imageUrl"),
            "토리",
            "먹기 좋아해요",
            LocalDate.of(2024, 9, 26),
            SizeType.SMALL,
            Gender.FEMALE,
            "https://i.pinimg.com/564x/08/01/67/080167a359545bc40068045f984a9994.jpg"
    );

    @PostMapping
    public ResponseEntity<ApiResponse<SavePlaygroundResponse>> save(
            @Auth Long memberId,
            @Valid @RequestBody SavePlaygroundRequest request
    ) {
        SavePlaygroundResponse savePlaygroundResponse = new SavePlaygroundResponse(
                1L,
                request.latitude(),
                request.longitude()
        );
        return ResponseEntity.created(URI.create("/playgrounds/" + 1))
                .body(ApiResponse.ofSuccess(savePlaygroundResponse));
    }

    @GetMapping("/{id}")
    public ApiResponse<FindPlaygroundDetailResponse> findById(@Auth Long memberId, @PathVariable Long id) {
        return ApiResponse.ofSuccess(new FindPlaygroundDetailResponse(
                        1L,
                        3,
                        0,
                        false,
                        List.of(
                                PlaygroundPetDetail.of(
                                        1L,
                                        dummyPet1,
                                        null,
                                        true,
                                        false
                                ),
                                PlaygroundPetDetail.of(
                                        1L,
                                        dummyPet2,
                                        null,
                                        true,
                                        false
                                ),
                                PlaygroundPetDetail.of(
                                        2L,
                                        dummyPet3,
                                        "7시에 가요~",
                                        false,
                                        false
                                )
                        )
                )
        );
    }

    @GetMapping("/{id}/summary")
    public ApiResponse<FindPlaygroundSummaryResponse> findSummaryById(@PathVariable Long id) {
        return ApiResponse.ofSuccess(new FindPlaygroundSummaryResponse(1L, 10, 4));
    }

    @GetMapping("/locations")
    public ApiResponse<List<FindPlaygroundLocationResponse>> findAllLocation(@Auth Long memberId) {
        return ApiResponse.ofSuccess(
                List.of(
                        new FindPlaygroundLocationResponse(1L, 37.5173316, 127.1011661, true),
                        new FindPlaygroundLocationResponse(2L, 37.5185122, 127.098778, false),
                        new FindPlaygroundLocationResponse(3L, 37.5136533, 127.0983182, false),
                        new FindPlaygroundLocationResponse(4L, 37.5131474, 127.1042528, false)
                )
        );
    }

    @PatchMapping("/arrival")
    public ApiResponse<UpdatePlaygroundArrivalResponse> updateArrival(
            @Auth Long memberId,
            @Valid @RequestBody UpdatePlaygroundArrivalRequest request
    ) {
        return ApiResponse.ofSuccess(new UpdatePlaygroundArrivalResponse(true));
    }
}
