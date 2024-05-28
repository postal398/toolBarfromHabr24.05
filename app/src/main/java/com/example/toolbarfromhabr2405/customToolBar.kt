import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun CollapsingHeader() {
    val density = LocalDensity.current

    var headerSize by remember { mutableStateOf(IntSize(0, 0)) }
    val headerHeight by remember(headerSize) { mutableStateOf(with(density) { headerSize.height.toDp() }) }

    var tabsSize by remember { mutableStateOf(IntSize(0, 0)) }
    val tabsHeight by remember(tabsSize) { mutableStateOf(with(density) { tabsSize.height.toDp() }) }

    val headerOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember(headerSize) {
        object : NestedScrollConnection {
            val headerHeightPx = headerSize.height.toFloat()

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = headerOffsetHeightPx.floatValue + delta
                headerOffsetHeightPx.floatValue = newOffset.coerceIn(-headerHeightPx, 0f)
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
                // Set zIndex to appear above LazyColumn. Alternatively, put LazyColumn above this Column
                .zIndex(1f)
                .offset { IntOffset(x = 0, y = headerOffsetHeightPx.floatValue.roundToInt()) }
                .background(MaterialTheme.colorScheme.surface),
        ) {
            // Collapsable top composable
            Header(modifier = Modifier.onSizeChanged { headerSize = it })
            // Composable that will take top of the screen when the list is scrolled up
            Tabs(modifier = Modifier.onSizeChanged { tabsSize = it })
        }
        LazyColumn(
            contentPadding = PaddingValues(top = headerHeight + tabsHeight)
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
        Text("Collapsing header")
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(value = "", onValueChange = {})
            Button(onClick = {}) { Text("Search") }
        }
    }
}

@Composable
fun Tabs(
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text("Something")
        TabRow(selectedTabIndex = 0) {
            Tab(selected = true, onClick = {}) { Text("Tab 0") }
            Tab(selected = false, onClick = {}) { Text("Tab 1") }
            Tab(selected = false, onClick = {}) { Text("Tab 2") }
        }
    }
}
