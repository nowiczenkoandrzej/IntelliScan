package com.example.objectdetection.feature_GPT.domain.repository

import com.example.objectdetection.core.domain.model.Resource

interface GPTRepository {

    suspend fun askGpt(prompt: String): Resource<String>

}

const val api_key = "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"