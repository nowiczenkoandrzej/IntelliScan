package com.example.objectdetection.feature_object_detection.presentation


import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.objectdetection.core.domain.model.Screen
import com.example.objectdetection.feature_object_detection.presentation.components.DetectedObjectsPanel
import com.example.objectdetection.feature_object_detection.presentation.components.objectDetection
import com.example.objectdetection.ui.theme.BgColor


@Composable
fun ObjectDetectionScreen(
    navController: NavController,
    viewModel: ObjectDetectionViewModel
) {

    val state by viewModel.screenState.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    LaunchedEffect(Unit) {
        cameraProviderFuture.addListener({
            cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        },ContextCompat.getMainExecutor(context))
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraProviderFuture.addListener({
                cameraProviderFuture.get()?.unbindAll()
            }, ContextCompat.getMainExecutor(context))
        }
    }

    objectDetection(
        lifecycleOwner = lifecycleOwner,
        cameraProviderFuture = cameraProviderFuture,
        onFrameAnalyzed = { newState ->
            viewModel.setState(newState)
        },
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(8.dp)

    ) {

        val halfHeight = maxHeight / 2


        /*

            Image from camera with detected object

         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .height(halfHeight)

        ) {
            if(state.analyzedBitmap != null){
                Image(
                    bitmap = state.analyzedBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        /*

            Panel below

         */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(halfHeight)
                .align(Alignment.BottomCenter)
        ) {
            DetectedObjectsPanel(
                objects = state.detectedObjects,
                onSummaryButtonClick = {
                    if(state.detectedObjects.isNotEmpty())
                        navController.navigate(
                            route = Screen.Summary.passObjectsNames(state.detectedObjects)
                        )
                    else {
                        Toast.makeText(context,"No item detected", Toast.LENGTH_SHORT).show()

                    }
                }
            )
        }


    }

}