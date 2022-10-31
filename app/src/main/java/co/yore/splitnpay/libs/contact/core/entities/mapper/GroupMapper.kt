package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Group
import co.yore.splitnpay.libs.contact.core.entities.cursor.GroupsCursor

internal class GroupMapper(private val groupsCursor: GroupsCursor) : EntityMapper<Group> {

    override val value: Group
        get() = Group(
            id = groupsCursor.id,
            systemId = groupsCursor.systemId,

            title = groupsCursor.title,

            readOnly = groupsCursor.readOnly,
            favorites = groupsCursor.favorites,
            autoAdd = groupsCursor.autoAdd,

            account = groupsCursor.account,

            isRedacted = false
        )
}
