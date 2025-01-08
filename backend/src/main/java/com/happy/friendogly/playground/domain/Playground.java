package com.happy.friendogly.playground.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Playground {

    protected static final int PLAYGROUND_RADIUS = 150;
    protected static final int MAX_OVERLAP_DISTANCE = PLAYGROUND_RADIUS * 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Location location;

    public Playground(Location location) {
        this.location = location;
    }

    public boolean isInsideBoundary(Location location) {
        return this.location.isWithin(location, PLAYGROUND_RADIUS);
    }

    public boolean isOverlapLocation(Location location) {
        return this.location.isWithin(location, MAX_OVERLAP_DISTANCE);
    }
}
