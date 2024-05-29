import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun CollapsingHeader() {

    var headerSize by remember { mutableStateOf(IntSize(0, 0)) } // Запоминаем размер заголовка

    val headerOffsetHeightPx = remember { mutableFloatStateOf(0f) } // Запоминаем текущее смещение заголовка
    val nestedScrollConnection = remember(headerSize) {
        object : NestedScrollConnection {
            val headerHeightPx = headerSize.height.toFloat() // Преобразуем высоту заголовка в пиксели

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y // Получаем смещение по оси Y
                val newOffset = headerOffsetHeightPx.floatValue + delta // Вычисляем новое смещение заголовка
                headerOffsetHeightPx.floatValue = newOffset.coerceIn(-headerHeightPx, 0f) // Ограничиваем смещение в пределах от -высоты заголовка до 0
                return Offset.Zero // Возвращаем смещение 0, так как все движение обработано
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize() // Заполняем весь размер контейнера
            .nestedScroll(nestedScrollConnection) // Добавляем возможность вложенного скролла
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .zIndex(1f) // Устанавливаем zIndex для отображения над LazyColumn
                .offset { IntOffset(x = 0, y = headerOffsetHeightPx.floatValue.roundToInt()) } // Смещаем заголовок по оси Y
                .background(MaterialTheme.colorScheme.surface) // Устанавливаем фоновый цвет из темы
        ) {
            Header(modifier = Modifier.onSizeChanged { headerSize = it }) // Вызываем Header и сохраняем его размер
        }
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = 56.dp // Добавляем нижний отступ для видимости последнего элемента
            )
        ) {
            items(100) { index ->
                Text(
                    text = "Item $index", // Текст элемента списка
                    modifier = Modifier
                        .fillMaxWidth() // Заполняем всю ширину
                        .padding(16.dp) // Добавляем внутренний отступ
                )
            }
        }
    }
}

@Composable
fun Header(
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth() // Заполняем всю ширину
            .background(Color.LightGray) // Устанавливаем светло-серый фоновый цвет
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Заполняем всю ширину
                .height(60.dp) // Устанавливаем фиксированную высоту
                .padding(top = 20.dp), // Добавляем верхний отступ
            horizontalArrangement = Arrangement.Center, // Выровняем содержимое по горизонтали по центру
            verticalAlignment = Alignment.CenterVertically // Выровняем содержимое по вертикали по центру
        ) {
            Box {
                Text("ТОНКАЯ НАСТРОЙКА") // Текст заголовка
            }
        }
    }
}
