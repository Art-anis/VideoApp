package com.example.videoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.videoapp.navigation.VideoItem
import com.example.videoapp.navigation.VideoList
import com.example.videoapp.ui.theme.VIdeoAppTheme
import com.example.videoapp.videoList.VideoListComponent
import com.example.videoapp.videoView.VideoViewComponent

//главная и единственная активность
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VIdeoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //установка навигационного контроллера
                    val navController = rememberNavController()

                    //строим навигационный граф
                    NavHost(
                        navController = navController,
                        startDestination = VideoList, //начинаем со списка видео
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        //список видео
                        composable<VideoList> {
                            VideoListComponent(
                                onNavigateToVideo = { video ->
                                    //создаем модель видео и посылаем ее дальше
                                    val item = VideoItem(
                                        title = video.name,
                                        id = video.id,
                                        description = video.description
                                    )
                                    //сама навигация
                                    navController.navigate(item)
                                }
                            )
                        }

                        //просмотр видео
                        composable<VideoItem> { entry ->
                            //извлекаем модель
                            val video: VideoItem = entry.toRoute()

                            //сам компонент для просмотра
                            VideoViewComponent(
                                video = video,
                                onNavigateBack = { //навигация назад
                                    navController.popBackStack()
                                },
                                onNavigateToOtherVideo = { listItem -> //навигация в другое видео
                                    val item = VideoItem(
                                        title = listItem.name,
                                        id = listItem.id,
                                        description = listItem.description
                                    )
                                    navController.navigate(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}