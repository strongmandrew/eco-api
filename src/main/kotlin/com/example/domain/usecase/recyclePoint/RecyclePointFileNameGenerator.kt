package com.example.domain.usecase.recyclePoint

import com.example.entity.RecyclePoint

class RecyclePointFileNameGenerator {

    companion object {
        private const val SEPARATOR = "&"
    }

    operator fun invoke(recyclePoint: RecyclePoint, extension: String):
            String {

        val items = listOf(recyclePoint.id.toString(), recyclePoint.streetName, recyclePoint.streetHouseNum)

        return items.joinToString(
            separator = SEPARATOR,
            postfix = ".$extension"
        )

    }
}