package com.example.objectdetection.feature_GPT.presentation


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.objectdetection.ui.theme.BgColor

@Composable
fun ChatGptScreen(
    navController: NavController,
    objectsNames: List<String>,
    viewModel: ChatGPTViewModel
) {

    Log.d("TAG", "ChatGptScreen: $objectsNames")


    LaunchedEffect(Unit) {
        viewModel.enterActivity(objectsNames)
    }

    val state by viewModel.screenState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(12.dp)

    ) {
        if(state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        LazyColumn() {
            items(
                items = state.ideaList
            ) {idea ->
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = idea,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.SansSerif,
                    )
                    Spacer(modifier = Modifier.height(8.dp) )
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp) )
                }
            }
        }

        state.error?.let { error ->
            Text(
                text = error,
                fontSize = 24.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Red
            )
        }
    }

}