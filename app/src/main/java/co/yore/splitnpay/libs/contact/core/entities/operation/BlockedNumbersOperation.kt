package co.yore.splitnpay.libs.contact.core.entities.operation

import android.content.ContentProviderOperation
import co.yore.splitnpay.libs.contact.core.BlockedNumbersFields
import co.yore.splitnpay.libs.contact.core.entities.NewBlockedNumber
import co.yore.splitnpay.libs.contact.core.entities.table.Table

private val TABLE = Table.BlockedNumbers

/**
 * Builds [ContentProviderOperation]s for [Table.BlockedNumbers].
 */
internal class BlockedNumbersOperation {

    fun insert(blockedNumber: NewBlockedNumber): ContentProviderOperation? =
        if (blockedNumber.number.isNullOrBlank()) { // The number is mandatory
            null
        } else {
            newInsert(TABLE)
                .withValue(BlockedNumbersFields.Number, blockedNumber.number)
                .withValue(BlockedNumbersFields.NormalizedNumber, blockedNumber.normalizedNumber)
                .build()
        }
}