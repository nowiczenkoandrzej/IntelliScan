package com.example.objectdetection.feature_GPT.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.objectdetection.core.domain.model.Resource
import com.example.objectdetection.feature_GPT.domain.model.GPTScreenState
import com.example.objectdetection.feature_GPT.domain.repository.GPTRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatGPTViewModel @Inject constructor(
    private val repository: GPTRepository
): ViewModel() {

    private val _screenState = MutableStateFlow(GPTScreenState())
    val screenState = _screenState.asStateFlow()

    fun enterActivity(objects: List<String>) {

        viewModelScope.launch {

            setState(isLoading = true)

            val prompt = createPrompt(objects = objects)

            val response = repository.askGpt(
                prompt = prompt
            )
            when(response) {
                is Resource.Success -> {
                    val ideas = divideResponse(response.data!!)
                    setState(
                        objects = objects,
                        gptResponse = response.data,
                        error = null,
                        isLoading = false,
                        ideas = ideas
                    )
                }
                is Resource.Error -> setState(
                    objects = objects,
                    gptResponse = null,
                    error = response.error,
                    isLoading = false
                )
            }

        }

    }

    private fun setState(
        objects: List<String> = emptyList(),
        gptResponse: String? = null,
        error: String? = null,
        isLoading: Boolean = false,
        ideas: List<String> = emptyList()
    ) {
        _screenState.value = GPTScreenState(
            objects = objects,
            gptResponse = gptResponse,
            error = error,
            isLoading = isLoading,
            ideaList = ideas
        )

    }

    private fun createPrompt(objects: List<String>): String {

        val sb = StringBuilder().also { sb ->
            objects.forEach { name ->
                sb.append("$name, ")
            }
        }

        return """
            I have few items such as: ${sb.toString()}.
            In what creative way can I connect those things to build something?
            Give me few ideas in list format like: 
            1. first idea
            2. second idea
            3. third idea 
            ...
        """.trimIndent()
    }

    private fun divideResponse(
        response: String
    ) = response
        .split("\n")
        .filter { text ->
            text.isNotEmpty()
        }
}