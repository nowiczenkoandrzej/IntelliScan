package com.example.objectdetection.feature_object_detection.presentation.components

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.objectdetection.FrameAnalysisCallback
import com.example.objectdetection.TensorFlowAnalyzer
import com.example.objectdetection.feature_object_detection.domain.model.ObjectDetectionScreenEvent
import com.example.objectdetection.feature_object_detection.domain.model.ObjectDetectionScreenState
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun objectDetection(
    lifecycleOwner: LifecycleOwner,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    onFrameAnalyzed: (ObjectDetectionScreenState) -> Unit,
) {
    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            preview.setSurfaceProvider(previewView.surfaceProvider)
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(
                    Size(
                        previewView.width,
                        previewView.height
                    )
                )
                .setBackpressureStrategy(
                    ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                )
                .build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                TensorFlowAnalyzer(
                    context = context,
                    callback = object : FrameAnalysisCallback {
                        override fun onFrameAnalyzed(state: ObjectDetectionScreenState) {
                            onFrameAnalyzed(state)
                        }

                    }
                )
            )

            try {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
            previewView
        },
        modifier = Modifier
            .alpha(0f)
    )

}

