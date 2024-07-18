package com.woowacourse.friendogly.pet.controller;

import com.woowacourse.friendogly.pet.dto.request.FindPetRequest;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.dto.response.FindPetResponse;
import com.woowacourse.friendogly.pet.service.PetCommandService;
import com.woowacourse.friendogly.pet.service.PetQueryService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> savePet(@RequestBody SavePetRequest savePetRequest) {
        Long petId = petCommandService.savePet(savePetRequest);
        return ResponseEntity.created(URI.create("/pets/" + petId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindPetResponse> findPetById(@PathVariable Long id) {
        FindPetResponse response = petQueryService.findPet(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mine/{memberId}")
    public ResponseEntity<List<FindPetResponse>> findPetsByMemberId(@PathVariable Long memberId) {
        List<FindPetResponse> pets = petQueryService.findPets(new FindPetRequest(memberId));
        return ResponseEntity.ok(pets);
    }
}

