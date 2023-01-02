package co.yore.splitnpay.libs

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val _events = MutableSharedFlow<Pair<*,*>>()
    val events = _events.asSharedFlow()

    suspend fun publish(event: Pair<*,*>) = _events.emit(event)
}