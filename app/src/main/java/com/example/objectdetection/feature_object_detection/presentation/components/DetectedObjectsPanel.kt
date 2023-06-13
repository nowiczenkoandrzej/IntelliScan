package com.example.objectdetection.feature_object_detection.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.objectdetection.ui.theme.BtnColor


@Composable
fun DetectedObjectsPanel(
    objects: ArrayList<String>,
    modifier: Modifier = Modifier,
    onSummaryButtonClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text(
                text = "Detected Objects:",
                fontSize = 24.sp,
                fontFamily = FontFamily.SansSerif
            )

            AnimatedItemList(
                objects = objects
            )
        }

        Button(
            onClick = onSummaryButtonClick,
            modifier = Modifier
                .align(Alignment.BottomEnd),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = BtnColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Summary",
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
            )
        }



    }


}
