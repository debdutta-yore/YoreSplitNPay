package co.yore.splitnpay.libs.jerokit.bottom_sheet

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import co.yore.splitnpay.libs.elseLet

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Sheeting.sheetContent(){
    val animDuration = remember{ 1000 }
    Box(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        AnimatedContent(
            targetState = this@sheetContent.sheets.value,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(animDuration)
                ) +
                    scaleIn(
                        initialScale = 1f,
                        animationSpec = tween(animDuration)
                    ) with
                    fadeOut(
                        animationSpec = tween(animDuration)
                    )
            }
        ) {
            this@sheetContent[it]
        }
    }
}

class Sheeting @OptIn(ExperimentalMaterialApi::class) constructor(
    private val skipHalfExpanded: Boolean = true,
    private val sheetMap: Map<Sheets, BottomSheetModel> = emptyMap(),
    private val onVisibilityChanged: (Boolean) -> Unit = {},
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = {true}
){
    private val _sheets = mutableStateOf(Sheets.None)
    val sheets get() = _sheets
    val map get() = sheetMap
    fun model(sheet: Sheets) = sheetMap[sheet]

    @Composable
    operator fun get(sheet: Sheets){
        return sheetMap[sheet].elseLet(elseBlock = { Text("") }) { it() }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private val _sheetHandler = SheetHandler(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded,
        confirmStateChange = confirmStateChange,
        onVisibilityChange = onVisibilityChanged
    )
    val sheetHandler get() = _sheetHandler

    @OptIn(ExperimentalMaterialApi::class)
    fun hide(){
        sheetHandler.state {
            hide()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun show(){
        sheetHandler.state {
            show()
        }
    }

    fun change(sheet: Sheets){
        _sheets.value = sheet
    }

    fun onBack(){
        sheetMap[sheets.value]?.onBack()
    }
}
