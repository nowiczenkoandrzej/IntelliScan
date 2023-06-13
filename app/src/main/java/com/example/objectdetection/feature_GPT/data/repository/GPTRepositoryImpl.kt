package com.example.objectdetection.feature_GPT.data.repository

import android.util.Log
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.objectdetection.core.domain.model.Resource
import com.example.objectdetection.feature_GPT.domain.repository.GPTRepository
import javax.inject.Inject

class GPTRepositoryImpl @Inject constructor(
    private val openAI: OpenAI
): GPTRepository {

    @OptIn(BetaOpenAI::class)
    override suspend fun askGpt(prompt: String): Resource<String> {

        try {

            Log.d("TAG", "askGpt: before everything: $prompt")
            val request = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.User,
                        content = prompt
                    )
                )
            )
            Log.d("TAG", "askGpt: after request")

            val completion: ChatCompletion = openAI.chatCompletion(request)

            Log.d("TAG", "askGpt: after completion")

            val response = completion.choices.first().message?.content

            Log.d("TAG", "askGpt: after response")

            return if(!response.isNullOrEmpty())
                Resource.Success(response)
            else
                Resource.Error("Ups... Something went wrong")

        } catch (e: Exception) {
            return if(!e.message.isNullOrEmpty())
                Resource.Error(e.message!!)
            else{
                Resource.Error("Ups... Something went wrong")
            }
        }
    }
}