package com.happy.friendogly.domain.model

enum class Gender(val petGender: String) {
    MALE("수컷"),
    FEMALE("암컷"),
    MALE_NEUTERED("중성화 수컷"),
    FEMALE_NEUTERED("중성화 암컷"),
}
