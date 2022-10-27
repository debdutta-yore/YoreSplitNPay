package co.yore.splitnpay.libs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Number.sx(color: Color? = null){
    if(color!=null){
        Spacer(modifier = Modifier.width(this.dep()).height(5.dep()).background(color))
    }
    else{
        Spacer(modifier = Modifier.width(this.dep()))
    }
}

@Composable
fun Number.sy(){
    Spacer(modifier = Modifier.height(this.dep()))
}