package com.example.campus_auto.ext

import android.content.Intent
import com.example.campus_auto.uimodel.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

fun combineLoadingState(
    scope: CoroutineScope,
    vararg other: StateFlow<LoadingState>
): StateFlow<LoadingState> {
    return combine(*other) { values: Array<LoadingState> ->
        if (values.all { it == LoadingState.Success }) {
            LoadingState.Success
        } else if (values.any { it == LoadingState.Error }) {
            LoadingState.Error
        } else {
            LoadingState.Loading
        }

    }.stateIn(
        scope,
        SharingStarted.Eagerly,
        LoadingState.Loading
    )
}

fun LocalDateTime.toEpochMilli():Long {
    return this.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
}

