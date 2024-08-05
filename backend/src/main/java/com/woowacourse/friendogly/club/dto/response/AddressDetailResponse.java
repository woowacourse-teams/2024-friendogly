package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.club.domain.Address;

public record AddressDetailResponse(
        String province,
        String city,
        String village
) {

    public AddressDetailResponse(Address address) {
        this(address.getProvince(), address.getCity(), address.getVillage());
    }
}
