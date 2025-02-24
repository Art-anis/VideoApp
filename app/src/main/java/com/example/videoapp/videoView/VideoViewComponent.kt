package com.example.videoapp.videoView

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.domain.models.VideoListItem
import com.example.videoapp.navigation.VideoItem
import com.example.videoapp.videoList.VideoItemComponent
import com.google.android.exoplayer2.ui.StyledPlayerView
import org.koin.androidx.compose.koinViewModel

//экран просмотра видео
@Composable
fun VideoViewComponent(
    video: VideoItem, //само видео
    onNavigateBack: () -> Unit, //функция возврата на предыдущий экран
    onNavigateToOtherVideo: (VideoListItem) -> Unit //функция перехода на другое видео
) {
    val configuration = LocalConfiguration.current //текущая конфигурация - используется для определения ориентации экрана

    val viewModel = koinViewModel<VideoViewViewModel>() //viewmodel
    val otherVideos = viewModel.otherVideos.collectAsState() //остальные видео из viewmodel

    //состояние жизненного цикла
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    //текущий владелец ЖЦ
    val lifecycleOwner = LocalLifecycleOwner.current
    //sideeffect, который меняется от одной стадии ЖЦ к другой
    DisposableEffect(lifecycleOwner) {
        //создаем observer
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        //добавляем его
        lifecycleOwner.lifecycle.addObserver(observer)

        //при выбрасывании эффекта отписываемся observer-ом
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    //внешний контейнер
    Box(
        //если мы в ландшафтной ориентации,то фон черный
        modifier = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            Modifier.background(Color.Black)
        else Modifier
    ) {
        //столбец для просматриваемого видео и других видео
        Column {
            //плеер
            AndroidView(
                //задаем, как создается объект
                factory = { context ->
                    StyledPlayerView(context).also {
                        //если мы только зашли, получаем видео из сети
                        if (viewModel.url.value == null) {
                            viewModel.prepareVideo(video.id)
                        }
                        //иначе, если мы возвращаемся, восстанавлиаем видео
                        else if (viewModel.expectingReturn) {
                            viewModel.onReturn()
                        }
                        //в любом случае соединяем плеер
                        it.player = viewModel.player
                    }
                },
                //функция обновления
                update = {
                    //зависит от стадии ЖЦ
                    when(lifecycle) {
                        //если onPause, то останавалиаем просмотр
                        Lifecycle.Event.ON_PAUSE -> {
                            it.onPause()
                            it.player?.pause()
                        }
                        //если onResume, вызываем onResume view
                        Lifecycle.Event.ON_RESUME -> {
                            it.onResume()
                        }
                        //иначе ничего не делаем
                        else -> Unit
                    }
                },
                //размеры экрана зависят от ориентации - либо 16/9 в портретной, либо на всю ширину в ландшафтной
                modifier = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                else Modifier.fillMaxWidth()
            )
            //только в портретной ориентации отображаем остальной контент
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //отступ от плеера
                Spacer(modifier = Modifier.height(16.dp))
                //название видео
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), //жирный шрифт
                    modifier = Modifier.padding(start = 8.dp) //отступ слева
                )
                //отступ между текстами
                Spacer(modifier = Modifier.height(16.dp))
                //описание видео
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
                //отступ
                Spacer(modifier = Modifier.height(16.dp))
                //список остальных видео
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    //такой же, как на экране просмотра списка видео
                    items(otherVideos.value) { video ->
                        Column {
                            VideoItemComponent(
                                item = video,
                                onNavigateToVideo = {
                                    //только отмечаем, что мы еще можем вернуться сюда во viewmodel
                                    viewModel.initiateLeave()
                                    onNavigateToOtherVideo(video)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
        //в ландшафтной ориентации отображаем название видео поверх ролика
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Text(
                text = video.title,
                maxLines = 1, //1 строка
                overflow = TextOverflow.Ellipsis, //переполнение многоточием
                style = MaterialTheme.typography.titleLarge,
                color = Color.White, //всегда белый цвет, т.к. на черном фоне
                modifier = Modifier.align(Alignment.TopStart) //размещаем слева сверху
                    .padding(start = 16.dp, top = 16.dp) //отступы от краев
            )
        }
    }

    //переопределяем поведение кнопки "Назад"
    BackHandler {
        //останавливаем плеер
        viewModel.endPlaying()
        //возвращаемся назад
        onNavigateBack()
    }
}