package co.yore.splitnpay.libs.kontakts.repo

import android.content.Context
import co.yore.splitnpay.libs.kontakts.models.MergedContact
import co.yore.splitnpay.libs.kontakts.suit.suitNamePhoneEmailImage
import javax.inject.Inject

class ContactRepo @Inject constructor(
    private val context: Context
) {
    suspend fun contacts(): List<MergedContact>{
        return suitNamePhoneEmailImage(context)
    }
}