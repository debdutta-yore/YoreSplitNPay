package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.BlockedNumber
import co.yore.splitnpay.libs.contact.core.entities.cursor.BlockedNumbersCursor

internal class BlockedNumberMapper(private val blockedNumbersCursor: BlockedNumbersCursor) :
    EntityMapper<BlockedNumber> {

    override val value: BlockedNumber
        get() = BlockedNumber(
            id = blockedNumbersCursor.id,

            number = blockedNumbersCursor.number,
            normalizedNumber = blockedNumbersCursor.normalizedNumber,

            isRedacted = false
        )
}
