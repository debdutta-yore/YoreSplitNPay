package co.yore.splitnpay.libs.jerokit

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController

data class ActivityService(
    private val context: Context
){
    fun toast(message: String, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(context, message, duration).show()
    }

    fun stringResource(@StringRes id: Int): String{
        return context.getString(id)
    }

    fun myAppSettingsPage(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun contentResolver(): ContentResolver = context.contentResolver
    fun context(): Context = context
}

typealias UIScope = suspend Bundle.(NavHostController, LifecycleOwner, ActivityService?) -> Unit

fun MutableState<UIScope?>.scope(block: UIScope?){
    this.value = {navHostController, lifecycleOwner, toaster ->
        block?.invoke(
            navHostController.currentBackStackEntry?.arguments ?: Bundle(),
            navHostController,
            lifecycleOwner,
            toaster
        )
        this@scope.value = null
    }
}

suspend fun MutableState<UIScope?>.forward(
    navHostController: NavHostController,
    lifecycleOwner: LifecycleOwner,
    activityService: ActivityService? = null
){
    this.value?.invoke(
        navHostController.currentBackStackEntry?.arguments ?: Bundle(),
        navHostController,
        lifecycleOwner,
        activityService
    )
}

fun Navigation() = mutableStateOf<UIScope?>(null)

fun NavHostController.set(
    route: String,
    key: String,
    value: Any?,
    last: Boolean = false
){
    if (last){
        backQueue
            .lastOrNull{ it.destination.route == route }
            ?.savedStateHandle
            ?.set(key, value)
    } else {
        backQueue
            .firstOrNull{ it.destination.route == route }
            ?.savedStateHandle
            ?.set(key, value)
    }
}

operator fun <T> NavHostController.get(key: String): T? {
    return currentBackStackEntry
        ?.savedStateHandle
        ?.get<T>(key)
}