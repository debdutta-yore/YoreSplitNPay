package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Relation
import co.yore.splitnpay.libs.contact.core.entities.cursor.RelationCursor

internal class RelationMapper(private val relationCursor: RelationCursor) :
    DataEntityMapper<Relation> {

    override val value: Relation
        get() = Relation(
            id = relationCursor.dataId,
            rawContactId = relationCursor.rawContactId,
            contactId = relationCursor.contactId,

            isPrimary = relationCursor.isPrimary,
            isSuperPrimary = relationCursor.isSuperPrimary,

            type = relationCursor.type,
            label = relationCursor.label,

            name = relationCursor.name,

            isRedacted = false
        )
}
