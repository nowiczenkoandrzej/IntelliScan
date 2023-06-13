package com.example.objectdetection.core.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.objectdetection.core.domain.model.OBJECT_NAMES_KEY
import com.example.objectdetection.core.domain.model.Screen
import com.example.objectdetection.feature_GPT.presentation.ChatGPTViewModel
import com.example.objectdetection.feature_GPT.presentation.ChatGptScreen
import com.example.objectdetection.feature_object_detection.presentation.ObjectDetectionViewModel
import com.example.objectdetection.feature_object_detection.presentation.ObjectDetectionScreen
import com.example.objectdetection.feature_object_detection.presentation.SummaryScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ObjectDetection.route
    ) {
        composable(
            route = Screen.ObjectDetection.route
        ) {
            val viewModel = hiltViewModel<ObjectDetectionViewModel>()
            ObjectDetectionScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.Summary.route,
            arguments = listOf(navArgument(OBJECT_NAMES_KEY) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val objectsNames = backStackEntry.arguments?.getString(OBJECT_NAMES_KEY)
            val names = objectsNames?.split(",") ?: emptyList()

           // val viewModel = hiltViewModel<SummaryViewModel>()

            SummaryScreen(
                navController = navController,
                objectsNames = names,
                //viewModel = viewModel
            )
        }

        composable(route = Screen.ChatGpt.route) { backStackEntry ->
            val objectsNames = backStackEntry.arguments?.getString(OBJECT_NAMES_KEY)
            val names = objectsNames?.split(",") ?: emptyList()

            val viewModel = hiltViewModel<ChatGPTViewModel>()

            ChatGptScreen(
                navController = navController,
                objectsNames = names,
                viewModel = viewModel
            )
        }
    }
}