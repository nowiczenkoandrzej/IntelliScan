package com.example.objectdetection.feature_object_detection.presentation


import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.objectdetection.core.domain.model.Screen
import com.example.objectdetection.ui.theme.BtnColor
import com.example.objectdetection.ui.theme.BgColor
import com.example.objectdetection.ui.theme.ItemColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SummaryScreen(
    navController: NavController,
    objectsNames: List<String>,
) {

    val mutableNames = remember { mutableStateListOf(*objectsNames.toTypedArray()) }

    // Empty string added at the end because last element in list is invisible
    // at beginning of item deleting animation
    LaunchedEffect(Unit) {
        mutableNames.add("")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(8.dp)

    ) {

        Text(
            text = "Objects in your room:",
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn() {
            items(
                items = mutableNames,
                key = { it }
            ) { name ->

                // condition because of last empty string element which is not intended to be shown
                if(name != "")
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(ItemColor)
                            .animateItemPlacement(
                                animationSpec = tween(400)
                            ),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier
                                .padding(start = 8.dp),
                        )

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    mutableNames.remove(name)

                                    // if list is empty navigate back to object detection screen
                                    // size must be equal 1 because of empty string at the end of list
                                    if (mutableNames.size == 1) {
                                        navController.popBackStack()
                                    }
                                }

                        )

                    }
                else
                    Spacer(modifier = Modifier.height(30.dp))

                Spacer(modifier = Modifier.height(6.dp))

            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // removing last element
                mutableNames.removeLast()
                navController.navigate(
                    route = Screen.ChatGpt.passObjectsNames(mutableNames)
                )
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = BtnColor,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "What can i do with these things?",
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

    }
}