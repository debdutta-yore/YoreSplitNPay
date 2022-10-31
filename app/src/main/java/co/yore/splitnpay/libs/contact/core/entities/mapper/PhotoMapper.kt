package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Photo
import co.yore.splitnpay.libs.contact.core.entities.cursor.PhotoCursor

internal class PhotoMapper(private val photoCursor: PhotoCursor) : DataEntityMapper<Photo> {

    override val value: Photo
        get() = Photo(
            id = photoCursor.dataId,
            rawContactId = photoCursor.rawContactId,
            contactId = photoCursor.contactId,

            isPrimary = photoCursor.isPrimary,
            isSuperPrimary = photoCursor.isSuperPrimary,

            fileId = photoCursor.photoFileId,

            isRedacted = false
        )
}
