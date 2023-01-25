package com.example.domain.usecase.recyclePoint

import com.example.entity.RecyclePoint
import io.ktor.http.*

class RecyclePointFileNameGenerator {

    companion object {
        private const val SEPARATOR = "&"
        private const val EXTENSION = ".jpg"
    }

    operator fun invoke(recyclePoint: RecyclePoint): String {

        val items = listOf(recyclePoint.id.toString(), recyclePoint.streetName, recyclePoint.streetHouseNum)

        return items.joinToString(
            separator = SEPARATOR,
            postfix = EXTENSION
        )

    }
}