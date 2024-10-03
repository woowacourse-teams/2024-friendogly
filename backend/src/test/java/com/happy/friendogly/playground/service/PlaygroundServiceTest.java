package com.happy.friendogly.playground.service;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.support.ServiceTest;
import java.time.LocalDate;

public abstract class PlaygroundServiceTest extends ServiceTest {

    protected Member saveMember(String name) {
        return memberRepository.save(
                new Member(name, "tag", "imageurl")
        );
    }

    protected Pet savePet(Member member) {
        return petRepository.save(
                new Pet(
                        member,
                        "petName",
                        "description",
                        LocalDate.of(2023, 10, 02),
                        SizeType.LARGE,
                        Gender.FEMALE,
                        "imgaeUrl"
                )
        );
    }

    protected Playground savePlayground() {
        return playgroundRepository.save(
                new Playground(new Location(37.5173316, 127.1011661))
        );
    }

    protected Playground savePlayground(double latitude, double longitude) {
        return playgroundRepository.save(
                new Playground(new Location(latitude, longitude))
        );
    }
}
