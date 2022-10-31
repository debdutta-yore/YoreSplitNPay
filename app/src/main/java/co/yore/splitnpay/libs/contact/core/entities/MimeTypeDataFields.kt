package co.yore.splitnpay.libs.contact.core.entities

import co.yore.splitnpay.libs.contact.core.DataField
import co.yore.splitnpay.libs.contact.core.EmptyDataFields
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.entities.custom.CustomDataRegistry

internal fun DataEntity.fields(customDataRegistry: CustomDataRegistry):
        Set<DataField> = mimeType.fields(customDataRegistry)

internal fun MimeType.fields(customDataRegistry: CustomDataRegistry): Set<DataField> =
    when (this) {
        MimeType.Address -> Fields.Address
        MimeType.Email -> Fields.Email
        MimeType.Event -> Fields.Event
        MimeType.GroupMembership -> Fields.GroupMembership
        MimeType.Im -> Fields.Im
        MimeType.Name -> Fields.Name
        MimeType.Nickname -> Fields.Nickname
        MimeType.Note -> Fields.Note
        MimeType.Organization -> Fields.Organization
        MimeType.Phone -> Fields.Phone
        MimeType.Photo -> Fields.Photo
        MimeType.Relation -> Fields.Relation
        MimeType.SipAddress -> Fields.SipAddress
        MimeType.Website -> Fields.Website
        is MimeType.Custom -> customDataRegistry.entryOf(this).fieldSet
        MimeType.Unknown -> EmptyDataFields
    }.all