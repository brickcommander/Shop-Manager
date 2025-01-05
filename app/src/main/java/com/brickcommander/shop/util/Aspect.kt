package com.brickcommander.shop.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun <T> coroutineAspect(block: suspend () -> T): T {
    return runBlocking {
        withContext(Dispatchers.IO) {
            block() // Execute and return the result
        }
    }
}
