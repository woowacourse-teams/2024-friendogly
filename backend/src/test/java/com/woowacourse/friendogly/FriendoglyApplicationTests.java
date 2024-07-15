package com.woowacourse.friendogly;

import com.woowacourse.friendogly.pet.domain.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FriendoglyApplicationTests {

	@Test
	void contextLoads() {
		Pet pet = Pet.builder()
				.name("땡이")
				.build();
	}

}
