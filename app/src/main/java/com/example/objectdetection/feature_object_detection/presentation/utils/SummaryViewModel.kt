package com.example.objectdetection.feature_object_detection.presentation.utils

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SummaryViewModel @Inject constructor(

): ViewModel() {

    private val _screenState = MutableStateFlow(ArrayList<String>())
    val screenState = _screenState.asStateFlow()

    fun setList(list: ArrayList<String>) {
        _screenState.value = list
    }

    fun removeItemFromList(item: String) {
        val currentList = _screenState.value
        currentList.remove(item)
        _screenState.value = currentList
    }

}