package com.happy.friendogly.club.dto.response;

import com.happy.friendogly.club.domain.Address;

public record AddressDetailResponse(
        String province,
        String city,
        String village
) {

    public AddressDetailResponse(Address address) {
        this(address.getProvince(), address.getCity(), address.getVillage());
    }
}
