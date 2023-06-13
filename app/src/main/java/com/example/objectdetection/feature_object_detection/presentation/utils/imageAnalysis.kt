package com.example.objectdetection.feature_object_detection.presentation.utils

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.objectdetection.FrameAnalysisCallback
import com.example.objectdetection.TensorFlowAnalyzer
import com.example.objectdetection.feature_object_detection.domain.model.ObjectDetectionScreenState
import java.util.concurrent.Executor
import java.util.concurrent.Executors

fun imageAnalysis(
    //cameraProvider: ProcessCameraProvider,
    context: Context,
    onFrameAnalysis: FrameAnalysisCallback
): ImageAnalysis {
    val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
        TensorFlowAnalyzer(
            context = context,
            callback = onFrameAnalysis
        )
        imageProxy.close()
    }

    return imageAnalysis
}