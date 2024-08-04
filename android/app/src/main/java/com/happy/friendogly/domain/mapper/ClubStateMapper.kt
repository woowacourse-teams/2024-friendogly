package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.ClubState

fun ClubState.toPresentation(): Boolean{
    return when(this){
        ClubState.OPEN  -> true
        ClubState.CLOSE -> false
    }
}
