package com.example.objectdetection.feature_object_detection.domain.model

import android.graphics.Bitmap

sealed class ObjectDetectionScreenEvent {

    data class SetAnalyzedBitmap(val bitmap: Bitmap): ObjectDetectionScreenEvent()
    data class NewObjectDetected(val name: String): ObjectDetectionScreenEvent()

}
