package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.axel_stein.pizzatestappcompose.domain.model.PizzaSize
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.OrderLayoutComponent.Description
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.OrderLayoutComponent.QuantityStepper
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.OrderLayoutComponent.SizeSelector
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.OrderLayoutComponent.Toolbar
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.model.PizzaImage
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.model.PizzaOrder
import com.axel_stein.pizzatestappcompose.ui.theme.AppColors
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private enum class OrderLayoutComponent {
    Toolbar,
    QuantityStepper,
    Description,
    SizeSelector
}

@Stable
private class OrderLayoutComponentData {
    var bounds: Rect? = null
    var translationY by mutableFloatStateOf(0f)

    override fun toString(): String {
        return "OrderLayoutComponentData(bounds=$bounds, translationY=$translationY)"
    }
}

@Composable
fun PizzaOrderLayout(
    pagerState: PagerState,
    items: ImmutableList<PizzaOrder>,
    currentItem: PizzaOrder,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    onSizeChanged: (PizzaSize) -> Unit,
    onNavigateUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues.Zero
) {
    val images by remember(items) {
        derivedStateOf {
            items.map {
                PizzaImage(
                    id = it.pizza.id,
                    url = it.pizza.imageUrl,
                    size = it.size
                )
            }.toImmutableList()
        }
    }

    val alphaAnimatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    val componentsCoordinates = remember {
        mutableMapOf<OrderLayoutComponent, OrderLayoutComponentData>().apply {
            OrderLayoutComponent.entries.forEach { put(it, OrderLayoutComponentData()) }
        }
    }

    var sizeFabCoordinates by remember {
        mutableStateOf(Offset.Zero)
    }

    var pizzaImageZoomProgress by remember { mutableFloatStateOf(0f) }

    val onSizeFabCoordinatesChanged: (LayoutCoordinates) -> Unit = remember { { coords ->
        if (coords.isAttached) {
            val bounds = coords.boundsInWindow(clipBounds = false)
            sizeFabCoordinates = bounds.center
        }
    } }

    val windowSize = LocalWindowInfo.current.containerSize
    val windowHeight = windowSize.height

    val onPizzaImageZoomed: (Float) -> Unit = remember { { zoom ->
        if (pizzaImageZoomProgress != zoom) {
            pizzaImageZoomProgress = zoom
            val translateFactor = 1.5f

            OrderLayoutComponent.entries.forEach { component ->
                componentsCoordinates[component]?.let { data ->
                    data.bounds?.let { bounds ->
                        if (component == Toolbar) {
                            val bottom = bounds.bottom
                            data.translationY = bottom * zoom * -translateFactor
                        } else {
                            val top = bounds.top
                            data.translationY = (windowHeight - top) * zoom * translateFactor
                        }
                    }
                }
            }
        }
    } }

    Column(
        modifier = modifier
            .graphicsLayer {
                alpha = alphaAnimatable.value
            }
            .drawWithCache {
            val path = Path()
            onDrawBehind {
                createArcPath(
                    path,
                    size,
                    sizeFabCoordinates,
                    dY = 65.dp.toPx()
                )
                drawPath(path, AppColors.Highlight)
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PizzaOrderToolbar(
            pizzaName = currentItem.pizza.name,
            onNavigateUpClick = onNavigateUpClick,
            modifier = Modifier
                .graphicsLayer {
                    componentsCoordinates[Toolbar]?.translationY?.let { ty ->
                        translationY = ty
                    }
                }
                .onGloballyPositioned {
                    componentsCoordinates[Toolbar]?.let { data ->
                        if (data.bounds == null) {
                            data.bounds = it.boundsInWindow(clipBounds = false)
                        }
                    }
                }
                .padding(top = innerPadding.calculateTopPadding())
        )

        PizzaImagePager(
            pagerState,
            images,
            onImageZoom = onPizzaImageZoomed,
            modifier = Modifier
                // .padding(top = 100.dp)
                .alpha(1f - pizzaImageZoomProgress)
        )

        PizzaSizeSelector(
            currentSize = currentItem.size,
            onSizeChanged = onSizeChanged,
            onCoordinatesChanged = onSizeFabCoordinatesChanged,
            modifier = Modifier
                .padding(top = 26.dp)
                .graphicsLayer {
                    componentsCoordinates[SizeSelector]?.translationY?.let { ty ->
                        translationY = ty
                    }
                }
                .onGloballyPositioned {
                    componentsCoordinates[SizeSelector]?.let { data ->
                        if (data.bounds == null) {
                            data.bounds = it.boundsInWindow(clipBounds = false)
                        }
                    }
                }
        )

        PizzaDescription(
            currentItem.pizza.description.orEmpty(),
            Modifier.graphicsLayer {
                    componentsCoordinates[Description]?.translationY?.let { ty ->
                        translationY = ty
                    }
                }
                .onGloballyPositioned {
                    componentsCoordinates[Description]?.let { data ->
                        if (data.bounds == null) {
                            data.bounds = it.boundsInWindow(clipBounds = false)
                        }
                    }
                }
                .padding(start = 40.dp, end = 20.dp, top = 20.dp)
                .weight(1f, fill = true)
        )

        QuantityStepper(
            currentItem.quantity,
            currentItem.price,
            onIncrement = onIncrementClick,
            onDecrement = onDecrementClick,
            modifier = Modifier
                .graphicsLayer {
                    componentsCoordinates[QuantityStepper]?.translationY?.let { ty ->
                        translationY = ty
                    }
                }
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 16.dp + innerPadding.calculateBottomPadding())
                .onGloballyPositioned {
                    componentsCoordinates[QuantityStepper]?.let { data ->
                        if (data.bounds == null) {
                            data.bounds = it.boundsInWindow(clipBounds = false)
                        }
                    }
                }
        )
    }
}

private fun createArcPath(
    path: Path,
    size: Size,
    pM: Offset,
    dY: Float
) {
    val endY = pM.y - dY

    path.reset()
    path.moveTo(0f, 0f)
    path.lineTo(0f, endY)

    val controlX = pM.x
    val controlY = 2 * pM.y - endY

    path.quadraticTo(
        controlX,
        controlY,
        size.width,
        endY
    )

    path.lineTo(size.width, 0f)
    path.close()
}