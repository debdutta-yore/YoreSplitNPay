package co.yore.splitnpay.libs

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf

class ResultingActivityHandler {
    private var _callback = mutableStateOf<(@Composable () -> Unit)?>(null)

    fun <I, O> request(
        contract: ActivityResultContract<I, O>
    ) {
        _callback.value = {
            val a = rememberLauncherForActivityResult(
                contract
            ) {

            }
            a.launch()
        }

        @Composable
        fun activityResult() {
            val a = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()
            )
            {

            }
            a.launch()
        }
    }
}