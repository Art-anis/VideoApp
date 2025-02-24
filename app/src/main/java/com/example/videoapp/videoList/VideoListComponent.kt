package com.example.videoapp.videoList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.models.VideoListItem
import org.koin.androidx.compose.koinViewModel

//экран списка видео
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListComponent(
    onNavigateToVideo: (VideoListItem) -> Unit //функция навигации к экрану просмотра видео
) {
    val viewModel = koinViewModel<VideoListViewModel>() //viewmodel

    val videos by viewModel.videos.observeAsState() //список видео
    val isLoading by viewModel.isLoading.collectAsState() //флаг загрузки для PtR

    videos?.let {
        //если список пустой, значит, он еще грузится
        if (it.isEmpty()) {
            //отображаем индикатор загрузки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        //иначе выводим сам список
        else {
            //компонент для PtR
            PullToRefreshBox(
                onRefresh = viewModel::getVideos, //функция обновления
                isRefreshing = isLoading, //флаг загрузки
            ) {
                //столбец с видео
                LazyColumn(
                    contentPadding = PaddingValues(16.dp) //отступы вокруг контента
                ) {
                    itemsIndexed(it) { index, video ->
                        //отображаем очередной элемент
                        Column {
                            VideoItemComponent(
                                item = video,
                                onNavigateToVideo = onNavigateToVideo
                            )
                            //пробел между соседними элементами
                            if (index != it.size - 1) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }

            }
        }
    }
}