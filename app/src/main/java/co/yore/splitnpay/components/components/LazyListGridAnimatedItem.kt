package co.yore.splitnpay.components.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class VisibleItem<T>(
    val visible: Boolean,
    val data: T,
    val state: State,
){
    enum class State{
        ADDING,
        REMOVING,
        ADDED,
        REMOVED
    }
}

class VisibilityList<T>(
    core: SnapshotStateList<T>,
    initialAnimated: Boolean = false,
    val identify: (T)->Any
){
    private var _originalSize = mutableStateOf(0)
    private val _visibilityList = mutableStateListOf<VisibleItem<T>>()
        .apply {
            val state = if(initialAnimated)
                VisibleItem.State.ADDING
            else
                VisibleItem.State.ADDED
            val visible = !initialAnimated
            addAll(
                core.map {
                    VisibleItem(
                        visible = visible,
                        data = it,
                        state = state
                    )
                }
            )
            _originalSize.value = core.size
        }
    val actualSize
        get() = _visibilityList.size
    val size
        get() = _originalSize
    val list: SnapshotStateList<VisibleItem<T>>
        get() = _visibilityList

    fun add(item: T){
        _visibilityList.add(
            VisibleItem(
                visible = false,
                data = item,
                state = VisibleItem.State.ADDING
            )
        )
        ++_originalSize.value
    }

    fun addAll(items: Iterable<T>, initialAnimated: Boolean = false){
        val state = if(initialAnimated)
            VisibleItem.State.ADDING
        else
            VisibleItem.State.ADDED
        val visible = !initialAnimated
        _visibilityList.addAll(items.map {
            VisibleItem(
                visible = visible,
                data = it,
                state = state
            )
        })
        _originalSize.value += items.count()
    }

    operator fun set(index: Int, item: T){
        val size = _visibilityList.size
        if(index in 0 until size){
            val visibleItem = _visibilityList[index]
            _visibilityList[index] = visibleItem.copy(data = item)
        }
    }

    fun add(index: Int, item: T){
        _visibilityList.add(index, VisibleItem(
            visible = false,
            data = item,
            state = VisibleItem.State.ADDING
        )
        )
        ++_originalSize.value
    }

    fun remove(item: T): T?{
        val index = _visibilityList.indexOfFirst {
            identify(it.data) == identify(item)
        }
        if(index > -1){
            _visibilityList[index] = VisibleItem(
                visible = false,
                data = item,
                state = VisibleItem.State.REMOVING
            )
            --_originalSize.value
            return item
        }
        return null
    }

    fun remove(block: (T)->Boolean): T?{
        val index = _visibilityList.indexOfFirst {
            block(it.data)
        }
        if(index > -1){
            val item = _visibilityList[index]
            _visibilityList[index] = VisibleItem(
                visible = false,
                data = item.data,
                state = VisibleItem.State.REMOVING
            )
            --_originalSize.value
            return item.data
        }
        return null
    }

    fun makeVisible(item: VisibleItem<T>){
        val index = _visibilityList.indexOf(item)
        if(index > -1){
            _visibilityList[index] = item.copy(
                visible = true,
                data = item.data,
                state = VisibleItem.State.ADDED
            )
        }
    }

    fun makeInvisible(item: VisibleItem<T>, duration: Long){
        val index = _visibilityList.indexOf(item)
        if(index > -1){
            _visibilityList[index] = item.copy(
                visible = false,
                data = item.data,
                state = VisibleItem.State.REMOVED
            )
            deleteAsync(item,duration)
        }
    }

    private var _deleting = false
    private val _deleteQue = mutableListOf<VisibleItem<T>>()
    private fun deleteAsync(item: VisibleItem<T>, duration: Long) {
        _deleteQue.add(item)
        if(_deleting){
            return
        }
        _deleting = true
        CoroutineScope(Dispatchers.IO).launch {
            if(duration>0){
                delay(duration)
                while(_deleteQue.size>0){
                    val _d = _deleteQue.removeAt(0)
                    dont_touch___delete(_d)
                }
                _deleting = false
            }
        }
    }

    fun dont_touch___delete(item: VisibleItem<T>) {
        val index = _visibilityList.indexOfFirst {
            identify(it.data) == identify(item.data)
        }
        _visibilityList.removeAt(index)
    }

    fun dont_touch___delete(index: Int) {
        _visibilityList.removeAt(index)
    }

    fun clear(animated: Boolean = false){
        if(!animated){
            _visibilityList.clear()
        }
        else{
            val list = _visibilityList.map {
                VisibleItem(
                    visible = false,
                    data = it.data,
                    state = VisibleItem.State.REMOVING
                )
            }
            _visibilityList.clear()
            _visibilityList.addAll(list)
        }
        _originalSize.value = 0
    }

    fun indexOf(item: T):Int {
        return _visibilityList.indexOfFirst {
            identify(it.data) == identify(item)
        }
    }

    fun indexOf(block: (T)->Boolean):Int {
        val index = _visibilityList.indexOfFirst {
            block(it.data)
        }
        return index
    }

    fun shuffle() {
        _visibilityList.shuffle()
    }

    fun isNotEmpty(): Boolean {
        return _originalSize.value!=0
    }

    fun removeAt(index: Int): Boolean {
        if(index !in 0.._visibilityList.size){
            return false
        }
        val item = _visibilityList[index]
        _visibilityList[index] = item.copy(
            visible = false,
            data = item.data,
            state = VisibleItem.State.REMOVING
        )
        --_originalSize.value
        return true
    }

    fun removeAll(items: Set<T?>) {
        items
    }

    fun <E>map(block: (T) -> E): List<E> {
        return _visibilityList.map {
            block(it.data)
        }
    }
}

fun <T>SnapshotStateList<T>.animated(
    animated: Boolean = false,
    identify: (T) -> Any
): VisibilityList<T>
{
        return VisibilityList(this,animated,identify)
    }

@OptIn(ExperimentalFoundationApi::class)
inline fun <T> LazyListScope.animatedItems(
    items: VisibilityList<T>,
    noinline key: ((item: T) -> Any)? = null,
    enter: EnterTransition = EnterTransition.None,
    exit: ExitTransition = ExitTransition.None,
    exitDuration: Long = 0,
    animateItemPlacementSpec: FiniteAnimationSpec<IntOffset>? = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
){
    items(
        items.list,
        key =
        if(key==null)
            null
        else {item: VisibleItem<T> ->
            key(item.data)
        }
    ){
        LaunchedEffect(key1 = it.visible){
            if(!it.visible&&it.state== VisibleItem.State.ADDING){
                items.makeVisible(it)
                return@LaunchedEffect
            }
            if(!it.visible&&it.state== VisibleItem.State.REMOVING){
                if(exitDuration>0){
                    items.makeInvisible(it,exitDuration)
                }
            }
        }
        AnimatedVisibility(
            it.visible,
            enter = enter,
            exit = exit,
            modifier = Modifier.then(if(animateItemPlacementSpec!=null) Modifier.animateItemPlacement(animateItemPlacementSpec) else Modifier)
        ) {
            itemContent(it.data)
        }
    }
}

/*
use this if not using compose foundation
@ExperimentalFoundationApi
inline fun <T> LazyGridScope.animatedItems(
    items: VisibilityList<T>,
    noinline spans: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    enter: EnterTransition = EnterTransition.None,
    exit: ExitTransition = ExitTransition.None,
    exitDuration: Long = 0,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
){
    items(
        items.list,
        spans = if(spans==null) null else {item: VisibleItem<T>->
            spans.invoke(this,item.data)
        }
    ){
        LaunchedEffect(key1 = it.visible){
            if(!it.visible&&it.state==VisibleItem.State.ADDING){
                items.makeVisible(it)
                return@LaunchedEffect
            }
            if(!it.visible&&it.state==VisibleItem.State.REMOVING){
                if(exitDuration>0){
                    items.makeInvisible(it)
                    delay(exitDuration)
                }
                items.dont_touch___delete(it)
            }
        }
        Box(){
            AnimatedVisibility(
                it.visible,
                enter = enter,
                exit = exit,
            ) {
                itemContent(it.data)
            }
        }
    }
}*/

@ExperimentalFoundationApi
inline fun <T> LazyGridScope.animatedItems(
    items: VisibilityList<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline span: (LazyGridItemSpanScope.(item: T) -> GridItemSpan)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable LazyGridItemScope.(item: T) -> Unit,
    enter: EnterTransition = EnterTransition.None,
    exit: ExitTransition = ExitTransition.None,
    exitDuration: Long = 0,
){
    items(
        items = items.list,
        key =
        if(key==null)
            null
        else
        {item: VisibleItem<T> ->
            key(item.data)
        },
        span =
        if(span==null)
            null
        else
        {item: VisibleItem<T> ->
            span(this,item.data)
        },
        contentType = {item: VisibleItem<T> ->
                      contentType(item.data)
        },
    ){
        LaunchedEffect(key1 = it.visible){
            if(!it.visible&&it.state== VisibleItem.State.ADDING){
                items.makeVisible(it)
                return@LaunchedEffect
            }
            if(!it.visible&&it.state== VisibleItem.State.REMOVING){
                if(exitDuration>0){
                    items.makeInvisible(it,exitDuration)
                    delay(exitDuration)
                }
                items.dont_touch___delete(it)
            }
        }
        Box(){
            AnimatedVisibility(
                it.visible,
                enter = enter,
                exit = exit,
            ) {
                itemContent(it.data)
            }
        }
    }
}
