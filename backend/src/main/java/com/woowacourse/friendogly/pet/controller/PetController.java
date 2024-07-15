package com.woowacourse.friendogly.pet.controller;

import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.service.PetCommandService;
import com.woowacourse.friendogly.pet.service.PetQueryService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
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
        return ResponseEntity.created(URI.create("/pet/" + petId)).build();
    }
}

