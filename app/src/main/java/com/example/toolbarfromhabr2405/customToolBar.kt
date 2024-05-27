import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Constraints

@Composable
fun CustomToolbar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints /*это ограничения*/ ->
        // Определяем размеры тулбара
        val toolbarHeight = 56.dp.roundToPx()
        val toolbarWidth = constraints.maxWidth

        // Измеряем дочерние элементы с учетом ограничений по ширине и высоте тулбара
        val placeables = measurables.map { measurable ->
            measurable.measure(Constraints.fixedWidth(toolbarWidth))
        }

        // Возвращаем результат измерения и компоновки
        layout(toolbarWidth, toolbarHeight) {
            placeables.forEach { placeable ->
                // Размещаем дочерние элементы
                placeable.placeRelative(x = 0, y = 0)
            }
        }
    }
}
