package co.yore.splitnpay

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.yore.splitnpay.libs.dep
import co.yore.splitnpay.locals.localDesignWidth

@Composable
    fun Number.SpaceX(fullWidth: Float = localDesignWidth.current){
        Spacer(modifier = Modifier.width(this.dep(fullWidth)))
    }

    @Composable
    fun Number.SpaceY(fullWidth: Float = localDesignWidth.current){
        Spacer(modifier = Modifier.height(this.dep(fullWidth)))
    }