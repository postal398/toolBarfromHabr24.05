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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun CollapsingHeader() {

    val density = LocalDensity.current

    var maxHeaderHeight by remember { mutableStateOf(with(density) { 130.dp.toPx() }) }
    var minHeaderHeight by remember { mutableStateOf(with(density) { 35.dp.toPx() }) }

    val headerHeightPercent = remember { mutableFloatStateOf(1f) } //замер изменений при скролле


    val headerHeight by remember(maxHeaderHeight) { mutableStateOf(with(density) { maxHeaderHeight.toDp() }) }


    val headerOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    val height =
        getHeight(
            min = 48.dp,
            max = with(LocalDensity.current) { maxHeaderHeight.toDp() },
            currentDp = with(LocalDensity.current) { headerOffsetHeightPx.floatValue.toDp() })

//    println("height = ${height.value}")

    val heightPx = with(density) { height.toPx() }

    val nestedScrollConnection = remember(maxHeaderHeight) {
        object : NestedScrollConnection {
            val headerHeightPx = maxHeaderHeight.toFloat()

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

//                println("delta = $delta")

                val newOffset = headerOffsetHeightPx.floatValue + delta

//                println("newOffset = $newOffset")

                // Ограничиваем смещение, чтобы заголовок не наезжал на статус-бар
                headerOffsetHeightPx.floatValue =
                    newOffset.coerceIn(-(headerHeightPx - minHeaderHeight), 0f)


//                println("header offset = ${headerOffsetHeightPx.floatValue}")

                //Обновляем переменную с замером изменений высоты
                headerHeightPercent.value = 1f + headerOffsetHeightPx.floatValue / (maxHeaderHeight - minHeaderHeight)

                println("Header height percent = ${headerHeightPercent.value}")


                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(Color.Red)
            .nestedScroll(nestedScrollConnection)
    ) {

        Column(
            modifier = Modifier
                .zIndex(1f)
//                .background(Color.Green)
                .height(height)
//                .offset { IntOffset(x = 0, y = headerOffsetHeightPx.floatValue.roundToInt()) }
                .background(Color.Yellow)
//                .height(height)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Header(modifier = Modifier
//                .onSizeChanged { maxHeaderHeight = it }
//                .onGloballyPositioned {
//                    println("onGloballyPositioned: ${it.size}")
//                    maxHeaderHeight = it.size
//                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .background(Color.Magenta),
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

private fun getHeight(min: Dp, max: Dp, currentDp: Dp): Dp {
//    println("getHeight: ${min.value} || ${max.value} || current = ${currentDp.value}")
    if (currentDp >= 0.dp) {
        return max
    }

    if ((max.value - currentDp.value.absoluteValue) <= min.value) {
        return min
    }

    return (max.value - currentDp.value.absoluteValue).dp
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
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp), // Заполняем ширину экрана
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
