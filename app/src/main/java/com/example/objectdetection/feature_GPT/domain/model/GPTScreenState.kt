package com.example.objectdetection.feature_GPT.domain.model

data class GPTScreenState(
    val objects: List<String> = emptyList(),
    val gptResponse: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val ideaList: List<String> = emptyList()
)
