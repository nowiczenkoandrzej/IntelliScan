package com.example.objectdetection.feature_object_detection.domain.model

import android.graphics.Bitmap

data class ObjectDetectionScreenState(
    val analyzedBitmap: Bitmap? = null,
    val detectedObjects: ArrayList<String> = ArrayList()
)
