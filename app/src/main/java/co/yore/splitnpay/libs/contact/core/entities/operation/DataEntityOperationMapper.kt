package co.yore.splitnpay.libs.contact.core.entities.operation

import android.content.ContentProviderOperation
import co.yore.splitnpay.libs.contact.core.AbstractDataField
import co.yore.splitnpay.libs.contact.core.ContactsException
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.entities.DataEntity
import co.yore.splitnpay.libs.contact.core.entities.ExistingDataEntity
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.custom.CustomDataRegistry
import co.yore.splitnpay.libs.contact.core.intersect

/**
 * Returns a new [ContentProviderOperation] for updating [this].
 *
 * Only the fields specified in [includeFields] will be updated.
 */
internal fun ExistingDataEntity.updateOperation(
    includeFields: Set<AbstractDataField>,
    customDataRegistry: CustomDataRegistry
): ContentProviderOperation? = dataOperation(includeFields, customDataRegistry)
    .updateDataRowOrDeleteIfBlank(this)

@Suppress("UNCHECKED_CAST")
private fun ExistingDataEntity.dataOperation(
    includeFields: Set<AbstractDataField>, customDataRegistry: CustomDataRegistry
): AbstractDataOperation<*, DataEntity> = when (mimeType) {
    // We could instead do when (this) is MutableAddress -> AddressOperation()
    // However, using mimeType instead of the class allows for exhaustive compilation checks.
    // Not requiring an 'else' branch.
    MimeType.Address -> AddressOperation(isProfile, Fields.Address.intersect(includeFields))
    MimeType.Email -> EmailOperation(isProfile, Fields.Email.intersect(includeFields))
    MimeType.Event -> EventOperation(isProfile, Fields.Event.intersect(includeFields))
    MimeType.Im -> ImOperation(isProfile, Fields.Im.intersect(includeFields))
    MimeType.Name -> NameOperation(isProfile, Fields.Name.intersect(includeFields))
    MimeType.Nickname -> NicknameOperation(isProfile, Fields.Nickname.intersect(includeFields))
    MimeType.Note -> NoteOperation(isProfile, Fields.Note.intersect(includeFields))
    MimeType.Organization -> OrganizationOperation(
        isProfile, Fields.Organization.intersect((includeFields))
    )
    MimeType.Phone -> PhoneOperation(isProfile, Fields.Phone.intersect(includeFields))
    MimeType.Relation -> RelationOperation(isProfile, Fields.Relation.intersect(includeFields))
    MimeType.SipAddress -> SipAddressOperation(
        isProfile, Fields.SipAddress.intersect(includeFields)
    )
    MimeType.Website -> WebsiteOperation(isProfile, Fields.Website.intersect(includeFields))
    is MimeType.Custom -> {
        val customDataEntry = customDataRegistry
            // Smart cast doesn't work here like this because mimeType has a custom getter. We can fix
            // this by declaring a local val mimeType = this.mimeType but this looks okay.
            .entryOf(mimeType as MimeType.Custom)

        customDataEntry.operationFactory
            .create(
                isProfile,
                customDataEntry.fieldSet.intersect(includeFields)
            )
    }
    // The GroupMembership and Photo class intentionally does not have a mutable version unlike the
    // other entities. Manage group memberships via the RawContactGroupMemberships extension
    // functions. Manage photos via the (Raw)ContactPhoto extension functions.
    MimeType.GroupMembership, MimeType.Photo, MimeType.Unknown ->
        throw ContactsException("No data operation found for ${mimeType.value}")
} as AbstractDataOperation<*, DataEntity>