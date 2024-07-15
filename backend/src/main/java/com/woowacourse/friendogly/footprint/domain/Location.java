package com.woowacourse.friendogly.footprint.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {

    private double latitude;
    private double longitude;
}
