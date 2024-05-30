import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
            .nestedScroll(nestedScrollConnection)

    ) {
        Column(
            modifier = Modifier
                .zIndex(1f)
                .offset { IntOffset(x = 0, y = headerOffsetHeightPx.floatValue.roundToInt()) }
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 24.dp) // Отступ для статус-бара
        ) {
            Header(modifier = Modifier.onSizeChanged { headerSize = it })
        }

        LazyColumn(
            contentPadding = PaddingValues(
                top = headerHeight + 24.dp,  // Добавляем высоту статус-бара
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

//    val statusBarLight = Color.Black //статус бар в светлой теме
//    val statusBarDark = Color.Red //хз
//    val navigationBarLight = Color.Black //цвет дедовского ниженего бара
//    val navigationBarDark = Color.White
//    val view = LocalView.current
//    val isDarkMod = isSystemInDarkTheme()
//
//    DisposableEffect(isDarkMod) {
//        val activity = view.context as Activity
//        activity.window.statusBarColor = if(isDarkMod){statusBarDark.toArgb()} else {statusBarLight.toArgb()}
//        activity.window.navigationBarColor = if(isDarkMod){navigationBarDark.toArgb()} else {navigationBarLight.toArgb()}
//
//        WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
//            isAppearanceLightStatusBars = !isDarkMod
//            isAppearanceLightNavigationBars = !isDarkMod
//        }
//
//        onDispose { }
//    }

}

@Composable
fun Header(
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Text("Collapsing header")
            }
        }
    }
}
