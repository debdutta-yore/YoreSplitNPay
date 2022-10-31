package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Nickname
import co.yore.splitnpay.libs.contact.core.entities.cursor.NicknameCursor

internal class NicknameMapper(private val nicknameCursor: NicknameCursor) : DataEntityMapper<Nickname> {

    override val value: Nickname
        get() = Nickname(
            id = nicknameCursor.dataId,
            rawContactId = nicknameCursor.rawContactId,
            contactId = nicknameCursor.contactId,

            isPrimary = nicknameCursor.isPrimary,
            isSuperPrimary = nicknameCursor.isSuperPrimary,

            name = nicknameCursor.name,

            isRedacted = false
        )
}
