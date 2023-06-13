package com.example.objectdetection.core.domain.model

const val OBJECT_NAMES_KEY = "objects_names"

sealed class Screen(val route: String) {

    object ObjectDetection: Screen(route = "object_detection")

    object ChatGpt: Screen(route = "chat_gpt/{$OBJECT_NAMES_KEY}") {
        fun passObjectsNames(
            names: List<String>
        ) = "chat_gpt/${names.joinToString(",")}"
    }

    object Summary: Screen(route = "summary/{$OBJECT_NAMES_KEY}") {

        fun passObjectsNames(
            names: List<String>
        ) = "summary/${names.joinToString(",")}"

    }
}
