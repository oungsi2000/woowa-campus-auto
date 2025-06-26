package com.example.campus_auto.ext

import com.example.campus_auto.uimodel.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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

