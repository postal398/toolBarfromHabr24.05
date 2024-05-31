import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import kotlin.math.roundToInt

@Composable
fun CollapsingHeader() {

    val density = LocalDensity.current
    val statusBarHeight = with(density) { 24.dp.toPx() }  // Высота статус-бара

    var headerSize by remember { mutableStateOf(IntSize(0, 0)) }
    val headerHeight by remember(headerSize) { mutableStateOf(with(density) { headerSize.height.toDp() }) }

    val headerOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember(headerSize) {
        object : NestedScrollConnection {
            val headerHeightPx = headerSize.height.toFloat()

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = headerOffsetHeightPx.floatValue + delta
                // Ограничиваем смещение, чтобы заголовок не наезжал на статус-бар
                headerOffsetHeightPx.floatValue = newOffset.coerceIn(-(headerHeightPx - statusBarHeight), 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .nestedScroll(nestedScrollConnection)

//            .padding(top = 30.dp)
//            .statusBarsPadding()

    ) {
        Column(
            modifier = Modifier
                .zIndex(1f)
                .offset { IntOffset(x = 0, y = headerOffsetHeightPx.floatValue.roundToInt()) }
                .background(MaterialTheme.colorScheme.surface)
//                .padding(top = 24.dp) // Отступ для статус-бара
        ) {
            Header(modifier = Modifier.onSizeChanged { headerSize = it })
        }

        LazyColumn(
            contentPadding = PaddingValues(
                top = headerHeight + 24.dp,
                bottom = 56.dp
            )
        ) {
            items(100) { index ->
                Text(
                    text = "Item $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        LinearProgressIndicator(
            progress = 0.8f, // Установка прогресса на 80%
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp) // Установка высоты прогресс-бара
        )
        Row(
            modifier = Modifier.fillMaxWidth(), // Заполняем ширину экрана
            horizontalArrangement = Arrangement.SpaceBetween, // Кнопки будут расположены слева и справа
            verticalAlignment = Alignment.Top
        ) {
            Button(onClick = { /*TODO*/ }) {}
            Button(onClick = { /*TODO*/ }) {}

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp) //высота статус-бара
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box() {
                Text("Collapsing header")
            }
        }
    }
}
