package com.example.videoapp.videoList

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.domain.models.VideoListItem
import com.example.videoapp.R

//функция форматирования времени для отображения продолжительности
private fun formatDuration(duration: String): String {
    //разбиваем строку формата hh:mm:ss.mmmm
    val timeArray = duration.split(":", ".").map { it.toLong() }
    //считаем количество секунд
    val time = timeArray[0] * 3600 + timeArray[1] * 60 + timeArray[2]
    //возвращаем строку в формате mm:ss или h:mm:ss
    return DateUtils.formatElapsedTime(time)
}

//элемент списка видео
@Composable
fun VideoItemComponent(
    item: VideoListItem, //сам элемент
    onNavigateToVideo: (VideoListItem) -> Unit //функции навигации на экран просмотра
) {
    //внешний контейнер - ряд
    Row(
        modifier = Modifier
            .fillMaxWidth() //занимает все свободное пространство
            .clickable { //весь контейнер кликабельный и переводит на страницу просмотра
                onNavigateToVideo(item)
            }
    ) {
        //коробка для миниатюры и продожительности видно
        Box {
            //миниатюра (используется плейсхолдер, т.к. api не возвращает миниатюру картинкой)
            Image(
                painter = painterResource(R.drawable.video_placeholder),
                contentDescription = null
            )
            //продолжительность видео
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd) //положение в коробке
                    .padding(bottom = 8.dp, end = 8.dp) //отступы
                    .background( //делаем фон полупрозрачным черным
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 4.dp), //отступы внутри фона
                text = formatDuration(item.duration),
                color = Color.White //цвет текста всегда белый
            )
        }
        //отступ между миниатюрой и названием
        Spacer(modifier = Modifier.width(16.dp))
        //название видео
        Text(
            text = item.name,
            maxLines = 2, //не более 2 строк
            overflow = TextOverflow.Ellipsis, //переполение отметить многоточием
            modifier = Modifier.fillMaxWidth() //заполняем всю оставшуюся ширину
        )
    }
}