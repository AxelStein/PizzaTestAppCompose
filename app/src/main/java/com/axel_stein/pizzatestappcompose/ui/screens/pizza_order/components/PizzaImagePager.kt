package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.axel_stein.pizzatestappcompose.R
import com.axel_stein.pizzatestappcompose.domain.model.PizzaSize
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.model.PizzaImage
import com.axel_stein.pizzatestappcompose.ui.theme.AppColors
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun PizzaImagePager(
    pagerState: PagerState,
    items: ImmutableList<PizzaImage>,
    onImageZoom: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val zoomState = rememberCustomPizzaImagePagerZoomState(scope)
    val onImageClick: (Int) -> Unit = remember { { page ->
        scope.launch {
            pagerState.animateScrollToPage(page, animationSpec = tween(400))
        }
    } }

    val currentImage = items.getOrNull(pagerState.currentPage)

    LaunchedEffect(zoomState.scale, onImageZoom) {
        onImageZoom(zoomState.scaleProgress)
    }

    BoxWithConstraints(modifier) {
        val imageSize = maxWidth * 0.7306f
        val paddingHorizontal = (maxWidth - imageSize) / 2

        Spacer(
            Modifier.padding(horizontal = paddingHorizontal)
                .size(imageSize)
                .scale(currentImage.scale)
                .onGloballyPositioned {
                    if (it.isAttached) {
                        zoomState.onAnchorPositioned(it)
                    }
                }
        )

        Box {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = zoomState.userScrollEnabled,
                contentPadding = PaddingValues(horizontal = paddingHorizontal),
                beyondViewportPageCount = 1
            ) { page ->
                val item = items.getOrNull(page)
                PizzaImagePage(
                    pagerState = pagerState,
                    page = page,
                    item = item,
                    imageSize = imageSize,
                    paddingHorizontal = paddingHorizontal,
                    modifier = Modifier
                        .clickable(interactionSource = null, indication = null) {
                            onImageClick(page)
                        }
                        .pointerInput(page) {
                            with(zoomState) {
                                handleZoomGesture()
                            }
                        }
                )
            }
            Image(
                painter = painterResource(R.drawable.circle_blur),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
            Image(
                painter = painterResource(R.drawable.ic_find),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (zoomState.isActive) {
            LocalFullscreenOverlay.current.show {
                ZoomablePizzaImageOverlay(
                    imageUrl = currentImage?.url,
                    zoomScale = zoomState.scale,
                    zoomOffset = zoomState.offset,
                    anchorSize = zoomState.anchorSize,
                    anchorOffset = zoomState.anchorOffset
                )
            }
        } else {
            LocalFullscreenOverlay.current.hide()
        }
    }
}

@Composable
private fun PizzaImagePage(
    pagerState: PagerState,
    page: Int,
    item: PizzaImage?,
    imageSize: Dp,
    paddingHorizontal: Dp,
    modifier: Modifier = Modifier
) {
    val imageSizePx = with(LocalDensity.current) {
        imageSize.toPx()
    }
    val paddingHorizontalPx = with(LocalDensity.current) {
        paddingHorizontal.toPx()
    }

    val targetScale = item.scale
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = Spring.StiffnessMedium
        )
    )

    PizzaImage(
        item = item,
        modifier = modifier
            .size(imageSize)
            .graphicsLayer {
                val pageOffset = page - (pagerState.currentPage + pagerState.currentPageOffsetFraction)
                val absPageOffset = pageOffset.absoluteValue

                val scale = lerp(
                    start = 0.3f,
                    stop = 1f,
                    fraction = 1f - absPageOffset.coerceIn(0f, 1f)
                )
                scaleX = scale
                scaleY = scale
                translationX = pageOffset * (imageSizePx - paddingHorizontalPx * 2f) / -2f
            }
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
    )
}

@Composable
private fun PizzaImage(item: PizzaImage?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(item?.url)
            .crossfade(true)
            .crossfade(durationMillis = 400)
            .memoryCacheKey(item?.url)
            .build(),
        contentDescription = null,
        modifier = modifier,
        placeholder = painterResource(R.drawable.pizza_placeholder),
        error = painterResource(R.drawable.pizza_placeholder)
    )
}

@Composable
private fun ZoomablePizzaImageOverlay(
    imageUrl: String?,
    zoomScale: Float,
    zoomOffset: Offset,
    anchorSize: Size,
    anchorOffset: Offset,
    modifier: Modifier = Modifier
) {
    var isLoaded by remember { mutableStateOf(false) }

    Box(modifier.fillMaxSize()) {
        if (isLoaded) {
            Spacer(
                Modifier
                    .layout { measurable, _ ->
                        val placeable = measurable.measure(
                            Constraints.fixed(
                                anchorSize.width.roundToInt(),
                                anchorSize.height.roundToInt()
                            )
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                                anchorOffset.x.roundToInt(),
                                anchorOffset.y.roundToInt()
                            )
                        }
                    }
                    .background(AppColors.Highlight)
            )
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .placeholderMemoryCacheKey(imageUrl)
                .listener(onStart = {
                    isLoaded = true
                })
                .size(coil3.size.Size.ORIGINAL)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .layout { measurable, _ ->
                    val placeable = measurable.measure(
                        Constraints.fixed(
                            anchorSize.width.roundToInt(),
                            anchorSize.height.roundToInt()
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .graphicsLayer {
                    translationX = zoomOffset.x
                    translationY = zoomOffset.y
                    scaleX = zoomScale
                    scaleY = zoomScale
                },
        )
    }
}

@Stable
private class CustomPizzaImagePagerZoomState(private val scope: CoroutineScope) {
    private val minScale = 1f

    var maxScale = 4f
        private set

    private var _anchorSize = Size.Zero
    private var _anchorOffset = Offset.Zero
    private var animateZoomDown = false

    val scaleProgress: Float
        get() = (scale - minScale) / (maxScale - minScale)

    var scale by mutableFloatStateOf(minScale)
        private set

    var offset by mutableStateOf(Offset.Zero)
        private set

    var isActive by mutableStateOf(false)
        private set

    val userScrollEnabled: Boolean
        get() = !isActive

    val anchorSize: Size
        get() = _anchorSize

    val anchorOffset: Offset
        get() = _anchorOffset

    fun onAnchorPositioned(it: LayoutCoordinates) {
        this._anchorSize = it.boundsInWindow(clipBounds = false).size
        this._anchorOffset = it.positionInWindow()
        if (!isActive) {
            this.offset = _anchorOffset
        }
    }

    suspend fun PointerInputScope.handleZoomGesture() = awaitEachGesture {
        awaitFirstDown()

        do {
            val event = awaitPointerEvent()
            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()

            val isZooming = zoomChange != minScale
            val isAlreadyZoomed = scale > minScale

            if (!animateZoomDown) {
                if (isZooming || isAlreadyZoomed || isActive) {
                    isActive = true
                    scale = (scale * zoomChange).coerceIn(minScale, maxScale)
                    offset += panChange
                    event.changes.forEach { it.consume() }
                }
            }
        } while (event.changes.any { it.pressed })

        if (isActive && !animateZoomDown) {
            animateZoomDown()
        }
    }

    private fun animateZoomDown() {
        animateZoomDown = true

        scope.launch {
            val scaleJob = launch {
                animate(
                    initialValue = scale,
                    targetValue = 1f,
                    animationSpec = tween(500)
                ) { value, _ ->
                    scale = value
                }
            }
            val offsetJob = launch {
                animate(
                    typeConverter = Offset.VectorConverter,
                    initialValue = offset,
                    targetValue = _anchorOffset,
                    animationSpec = tween(500)
                ) { value, _ ->
                    offset = value
                }
            }
            scaleJob.join()
            offsetJob.join()
            isActive = false
            animateZoomDown = false
        }
    }
}

@Composable
private fun rememberCustomPizzaImagePagerZoomState(scope: CoroutineScope): CustomPizzaImagePagerZoomState {
    return remember {
        CustomPizzaImagePagerZoomState(scope)
    }
}

private val PizzaImage?.scale: Float
    get() = when (this?.size) {
        PizzaSize.Small -> 0.7f
        PizzaSize.Medium -> 0.9f
        else -> 1f
    }