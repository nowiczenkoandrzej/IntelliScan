package com.example.objectdetection.feature_GPT.domain.repository

import com.example.objectdetection.core.domain.model.Resource

interface GPTRepository {

    suspend fun askGpt(prompt: String): Resource<String>

}
