package com.axel_stein.pizzatestappcompose.ui.screens.pizza_order

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.axel_stein.pizzatestappcompose.domain.model.PizzaSize
import com.axel_stein.pizzatestappcompose.domain.repository.PizzaRepository
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.ErrorLayout
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.PizzaOrderLayout
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.PizzaSplash
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.components.PizzaSplashState
import com.axel_stein.pizzatestappcompose.ui.screens.pizza_order.model.PizzaOrder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.koinInject

@Composable
fun PizzaOrderScreen(modifier: Modifier = Modifier) {
    val repository = koinInject<PizzaRepository>()

    var refetchData by remember { mutableStateOf(false) }
    var items by remember { mutableStateOf<ImmutableList<PizzaOrder>>(persistentListOf()) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val pagerState = rememberPagerState(pageCount = { items.size })
    val currentItem by remember(items) {
        derivedStateOf {
            items.getOrNull(pagerState.currentPage)
        }
    }
    val refresh: () -> Unit = remember { {
        refetchData = !refetchData
    } }
    val onIncrementClick: () -> Unit = remember { {
        items = updateItemAt(items, index = pagerState.currentPage) {
            it.copy(quantity = it.quantity + 1)
        }
    } }
    val onDecrementClick: () -> Unit = remember { {
        items = updateItemAt(items, index = pagerState.currentPage) {
            if (it.quantity > 1) {
                it.copy(quantity = it.quantity - 1)
            } else {
                it
            }
        }
    } }
    val onSizeChanged: (PizzaSize) -> Unit = remember(items) { { size ->
        items = updateItemAt(items, index = pagerState.currentPage) {
            it.copy(size = size)
        }
    } }
    val onNavigateUpClick: () -> Unit = remember(refetchData) { {
        refetchData = !refetchData
    } }
    var splashState by remember { mutableStateOf(PizzaSplashState.Running) }
    val onSplashStateChanged: (PizzaSplashState) -> Unit = remember { {
        if (splashState != it) {
            splashState = it
        }
    } }

    LaunchedEffect(refetchData) {
        items = persistentListOf()
        splashState = PizzaSplashState.Running
        error = null

        repository.getPizzas()
            .onSuccess {
                items = it.map { pizza ->
                    PizzaOrder(
                        pizza = pizza,
                        quantity = 1,
                        size = pizza.defaultSize ?: PizzaSize.Medium
                    )
                }.toImmutableList()
            }
            .onFailure {
                error = it
            }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (splashState != PizzaSplashState.Ended) {
            PizzaSplash(
                restartOnEnd = items.isEmpty() && error == null,
                onStateChanged = onSplashStateChanged
            )
        }
        if (splashState != PizzaSplashState.Running) {
            when {
                error != null -> ErrorLayout(error!!, onRetryClick = refresh)
                currentItem != null -> PizzaOrderLayout(
                    pagerState,
                    items,
                    currentItem!!,
                    onIncrementClick,
                    onDecrementClick,
                    onSizeChanged,
                    onNavigateUpClick,
                    modifier,
                    innerPadding
                )
            }
        }
    }
}

private fun updateItemAt(
    items: ImmutableList<PizzaOrder>,
    index: Int,
    transform: (PizzaOrder) -> PizzaOrder
): ImmutableList<PizzaOrder> {
    items.getOrNull(index)?.let { item ->
        return items.toMutableList().apply {
            this[index] = transform(item)
        }.toImmutableList()
    }
    return items
}