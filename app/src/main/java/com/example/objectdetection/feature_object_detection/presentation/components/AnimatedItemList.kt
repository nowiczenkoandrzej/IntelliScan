package com.example.objectdetection.feature_object_detection.presentation.components


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.objectdetection.ui.theme.ItemColor

@Composable
fun AnimatedItemList(
    objects: List<String>,
    isDeleteButtonVisible: Boolean = false,
    onDeleteButtonClick: (objectName: String) -> Unit = {}
) {




    if(objects.isEmpty()) {
        Text(
            text = "No item has been detected",
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(8.dp)
        )
    } else {
        LazyColumn(
            content = {
                items(objects.size, key = { it }) { index ->

                    val item = remember { mutableStateOf(objects[index]) }

                    var visible by remember { mutableStateOf(false) }

                    LaunchedEffect(key1 = item.value) {
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(500)
                        ) + fadeIn(),
                        exit = slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(500)
                        ) + fadeOut()
                    ) {

                        Text(
                            text = item.value,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier

                                .clip(RoundedCornerShape(4.dp))
                                .background(ItemColor)
                                .padding(4.dp),

                        )

                    }
                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                }
            }
        )
    }


}
