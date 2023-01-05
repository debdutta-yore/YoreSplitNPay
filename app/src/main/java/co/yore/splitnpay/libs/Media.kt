package co.yore.splitnpay.libs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

class Media(private val context: Context) {
    fun decodeBitmapFromUri(uri: Uri): Bitmap{
        return BitmapFactory
            .decodeStream(
                context
                .contentResolver
                .openInputStream(uri)
            )
    }
}