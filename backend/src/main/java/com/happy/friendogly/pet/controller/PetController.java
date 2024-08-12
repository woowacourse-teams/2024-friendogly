package com.happy.friendogly.pet.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.pet.dto.request.SavePetRequest;
import com.happy.friendogly.pet.dto.response.FindPetResponse;
import com.happy.friendogly.pet.dto.response.SavePetResponse;
import com.happy.friendogly.pet.service.PetCommandService;
import com.happy.friendogly.pet.service.PetQueryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetQueryService petQueryService;
    private final PetCommandService petCommandService;

    public PetController(PetQueryService petQueryService, PetCommandService petCommandService) {
        this.petQueryService = petQueryService;
        this.petCommandService = petCommandService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SavePetResponse>> savePet(
            @Auth Long memberId,
            @RequestPart @Valid SavePetRequest request,
            @RequestPart(required = false) MultipartFile image
    ) {
        SavePetResponse response = petCommandService.savePet(memberId, request, image);
        return ResponseEntity.created(URI.create("/pets/" + response.id()))
                .body(ApiResponse.ofSuccess(response));
    }

    @GetMapping("/{id}")
    public ApiResponse<FindPetResponse> findById(@PathVariable Long id) {
        FindPetResponse response = petQueryService.findById(id);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/mine")
    public ApiResponse<List<FindPetResponse>> findMine(@Auth Long memberId) {
        List<FindPetResponse> response = petQueryService.findByMemberId(memberId);
        return ApiResponse.ofSuccess(response);
    }

    @GetMapping
    public ApiResponse<List<FindPetResponse>> findByMemberId(@RequestParam Long memberId) {
        List<FindPetResponse> response = petQueryService.findByMemberId(memberId);
        return ApiResponse.ofSuccess(response);
    }
}
