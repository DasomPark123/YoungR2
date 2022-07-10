package com.nutrient.youngr2.utils

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi

/* 클릭 이벤트를 flow 로 변환 */
fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        this.trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}

/* 에디터 이벤트를 flow로 변환 */
fun TextView.imeClicks(event : Int) : Flow<Unit> = callbackFlow {
    setOnEditorActionListener { textView, actionId, keyEvent ->
        var handled = false
        when (actionId) {
            event -> {
                this.trySend(Unit)
                handled = true
            }
        }
        handled
    }
    awaitClose { setOnEditorActionListener(null) }
}

/* View 클릭 시 중복 클릭 방지 */
fun View.setClickEvent(
    uiScope: CoroutineScope,
    windowDuration: Long = THROTTLE_DURATION,
    onClick : () -> Unit
) {
    clicks()
        .throttleFirst(windowDuration)
        .onEach { onClick.invoke() }
        .launchIn(uiScope)
}

/* Keyboard 버튼 클릭 시 중복 클릭 방지 */
fun TextView.setEditorEvent(
    uiScope: CoroutineScope,
    actionId : Int,
    windowDuration: Long = THROTTLE_DURATION,
    onActionChanged : () -> Unit
) {
    imeClicks(actionId)
        .throttleFirst(windowDuration)
        .onEach { onActionChanged.invoke() }
        .launchIn(uiScope)
}

