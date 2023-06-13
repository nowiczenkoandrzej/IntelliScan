package com.example.objectdetection.feature_object_detection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.objectdetection.feature_object_detection.domain.model.ObjectDetectionScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ObjectDetectionViewModel @Inject constructor(

): ViewModel() {

    private val visibleObjects = mutableMapOf<String, Job>()

    private val _screenState = MutableStateFlow(ObjectDetectionScreenState())
    val screenState = _screenState.asStateFlow()


    fun setState(state: ObjectDetectionScreenState) {
        val newList = addNewObjects(state.detectedObjects)

        _screenState.value = ObjectDetectionScreenState(
            analyzedBitmap = state.analyzedBitmap,
            detectedObjects = newList
        )
    }


    private fun addNewObjects(objects: ArrayList<String>): ArrayList<String> {

        if(screenState.value.detectedObjects.isEmpty()){
            objects.forEach { key ->
                visibleObjects[key] = viewModelScope.launch {
                    delay(3000)
                    removeOldObject(key)
                }
            }
            return objects
        }

        val newList = ArrayList<String>()

        objects.forEach { newObject ->

            val isThereJobToRemoveObject = visibleObjects.keys.contains(newObject)

            if(isThereJobToRemoveObject)
                visibleObjects[newObject]?.cancel()


            visibleObjects[newObject] = viewModelScope.launch {
                delay(3000)
                removeOldObject(newObject)
            }

        }
        visibleObjects.keys.forEach {
            newList.add(it)
        }

        return newList
    }

    private fun removeOldObject(name: String) {
        visibleObjects.remove(name)
    }

}


