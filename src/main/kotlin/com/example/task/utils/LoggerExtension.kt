package com.example.task.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LoggerExtension {
    inline fun <reified T> T.getLogger(): Logger =
        LoggerFactory.getLogger(
            if (T::class.isCompanion) T::class.java.enclosingClass else T::class.java
        )
}
