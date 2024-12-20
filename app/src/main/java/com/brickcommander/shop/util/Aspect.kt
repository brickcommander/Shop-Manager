package com.brickcommander.shop.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun <T> coroutineAspect(block: suspend () -> T): T {
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }
    return Unit as T
}
