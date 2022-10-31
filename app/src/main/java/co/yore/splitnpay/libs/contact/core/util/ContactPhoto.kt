package co.yore.splitnpay.libs.contact.core.util

import android.content.ContentProviderOperation.newDelete
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.ContactsContract
import co.yore.splitnpay.libs.contact.core.*
import co.yore.splitnpay.libs.contact.core.entities.ExistingContactEntity
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.MutableContact
import co.yore.splitnpay.libs.contact.core.entities.TempRawContact
import co.yore.splitnpay.libs.contact.core.entities.cursor.contactsCursor
import co.yore.splitnpay.libs.contact.core.entities.cursor.dataCursor
import co.yore.splitnpay.libs.contact.core.entities.mapper.tempRawContactMapper
import co.yore.splitnpay.libs.contact.core.entities.operation.withSelection
import co.yore.splitnpay.libs.contact.core.entities.table.ProfileUris
import co.yore.splitnpay.libs.contact.core.entities.table.Table
import java.io.IOException
import java.io.InputStream

// region GET PHOTO

/**
 * Returns the full-sized photo as an [InputStream].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoUri] from this instance. Instead, a query is
 * made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * It is up to the caller to close the [InputStream].
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoInputStream(contacts: Contacts): InputStream? {
    if (!contacts.permissions.canQuery()) {
        return null
    }

    return contacts.contentResolver.query(
        if (isProfile) ProfileUris.CONTACTS.uri else Table.Contacts.uri,
        Include(ContactsFields.PhotoUri),
        ContactsFields.Id equalTo id
    ) {
        val photoUri = it.getNextOrNull { it.contactsCursor().photoUri }
        uriInputStream(contacts, photoUri)
    }
}

/**
 * Returns the full-sized photo as a [ByteArray].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoUri] from this instance. Instead, a query is
 * made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoBytes(contacts: Contacts): ByteArray? =
    photoInputStream(contacts)?.apply {
        it.readBytes()
    }

/**
 * Returns the full-sized photo as a [Bitmap].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoUri] from this instance. Instead, a query is
 * made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoBitmap(contacts: Contacts): Bitmap? =
    photoInputStream(contacts)?.apply {
        BitmapFactory.decodeStream(it)
    }

/**
 * Returns the full-sized photo as a [BitmapDrawable].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoUri] from this instance. Instead, a query is
 * made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoBitmapDrawable(contacts: Contacts): BitmapDrawable? =
    photoInputStream(contacts)?.apply {
        BitmapDrawable(contacts.resources, it)
    }

private fun uriInputStream(contacts: Contacts, uri: Uri?): InputStream? {
    if (uri == null) {
        return null
    }

    var inputStream: InputStream? = null
    try {
        val fd = contacts.contentResolver.openAssetFileDescriptor(uri, "r")
        inputStream = fd?.createInputStream()
    } catch (ioe: IOException) {
        // do nothing
    }
    return inputStream
}

// endregion

// region GET PHOTO THUMBNAIL

/**
 * Returns the photo thumbnail as an [InputStream].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoThumbnailUri] from this instance. Instead, a
 * query is made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * It is up to the caller to close the [InputStream].
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoThumbnailInputStream(contacts: Contacts): InputStream? {
    if (!contacts.permissions.canQuery()) {
        return null
    }

    return contacts.contentResolver.query(
        if (isProfile) ProfileUris.CONTACTS.uri else Table.Contacts.uri,
        Include(ContactsFields.PhotoThumbnailUri),
        ContactsFields.Id equalTo id
    ) {
        val photoThumbnailUri = it.getNextOrNull { it.contactsCursor().photoThumbnailUri }
        uriInputStream(contacts, photoThumbnailUri)
    }
}

/**
 * Returns the photo thumbnail as a [ByteArray].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoThumbnailUri] from this instance. Instead, a
 * query is made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoThumbnailBytes(contacts: Contacts): ByteArray? =
    photoThumbnailInputStream(contacts)?.apply {
        it.readBytes()
    }

/**
 * Returns the full-sized photo as a [Bitmap].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoThumbnailUri] from this instance. Instead, a
 * query is made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoThumbnailBitmap(contacts: Contacts): Bitmap? =
    photoThumbnailInputStream(contacts)?.apply {
        BitmapFactory.decodeStream(it)
    }

/**
 * Returns the full-sized photo as a [BitmapDrawable].
 *
 * Returns null if a photo has not yet been set or if permissions have not been granted or if the
 * contact no longer exists.
 *
 * This does not use the [ExistingContactEntity.photoThumbnailUri] from this instance. Instead, a
 * query is made to retrieve the latest photo uri from the database in order to ensure validity.
 *
 * This photo is picked from one of the associated [android.provider.ContactsContract.RawContacts]s
 * by the Contacts Provider, which may not be in the list of [ExistingContactEntity.rawContacts]
 * depending on query filters.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.READ_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.photoThumbnailBitmapDrawable(contacts: Contacts): BitmapDrawable? =
    photoThumbnailInputStream(contacts)?.apply {
        BitmapDrawable(contacts.resources, it)
    }

// endregion

// region SET PHOTO

/**
 * Sets the photo of this [MutableContact.primaryPhotoHolder], pending an update API call. If a
 * photo already exists, it will be overwritten. The Contacts Provider automatically creates a
 * downsized version of this as the thumbnail.
 *
 * If a photo has not yet been set and the Contacts Provider has not yet chosen the RawContact that
 * will be used as the primary photo holder, then this will use the first RawContact in the list of
 * [MutableContact.rawContacts].
 *
 * The given [photoData] will not be set until the update API call is committed successfully.
 *
 * If you want to directly set the photo into the database, without an update API call, use
 * [MutableContact.setPhotoDirect].
 *
 * Note that the [MutableContact.primaryPhotoHolder] requires [MutableContact.photoFileId].
 *
 * ## No includes required
 *
 * When using update APIs, there is is no field required to be passed into the `include` function
 * to make sure the photo is set as long as this function is invoked.
 *
 * ## Not parcelable
 *
 * The [photoData] is ignored on parcel. The [photoData] will not be carried over across activities,
 * fragments, or views during creation/recreation.
 */
fun MutableContact.setPhoto(photoData: PhotoData) {
    primaryPhotoHolder?.photoDataOperation = PhotoDataOperation.SetPhoto(photoData)
}

// endregion

// region SET PHOTO DIRECT

/**
 * Sets the photo of this [ExistingContactEntity] (and the [contacts.core.entities.RawContact] that
 * the Contacts Provider has chosen to hold the primary photo) directly to the database. If a photo
 * already exists, it will be overwritten. The Contacts Provider automatically creates a downsized
 * version of this as the thumbnail.
 *
 * If a photo has not yet been set and the Contacts Provider has not yet chosen the RawContact that
 * will be used as the primary photo holder, then this will use the first RawContact in the list of
 * [ExistingContactEntity.rawContacts].
 *
 * Returns true if the operation succeeds.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Changes are immediate
 *
 * This function will make the changes to the Contacts Provider database immediately. You do not
 * need to use update APIs to commit the changes.
 *
 * ## Changes are not applied to the receiver
 *
 * This function call does NOT mutate immutable or mutable receivers. Therefore, you should use
 * query APIs or refresh extensions or process the result of this function call to get the most
 * up-to-date reference to mutable or immutable entity that contains the changes in the Contacts
 * Provider database.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.WRITE_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 *
 * ## DEV NOTES
 *
 * The function body is mostly taken from the sample code from the
 * [ContactsContract.RawContacts.DisplayPhoto] class documentation.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
fun ExistingContactEntity.setPhotoDirect(contacts: Contacts, photoData: PhotoData): Boolean {
    if (!contacts.permissions.canUpdateDelete()) {
        return false
    }

    val photoFileId = photoFileId(contacts)

    val rawContact = if (photoFileId != null) {
        // A photo exists for the Contact. Get the RawContact in the Data table that holds the photo
        // row with the same photo file id. Keep in mind that there may be multiple RawContacts
        // associated with a single Contact.
        rawContactWithPhotoFileId(contacts, photoFileId)
    } else {
        // No photo exists for the Contact or any of its associated RawContacts. Use the
        // first RawContact as the default or fail if also not available.
        rawContacts.firstOrNull()
    }

    return rawContact?.id?.let { rawContactId ->
        contacts.setRawContactPhotoDirect(rawContactId, photoData)
    } == true
}

private fun ExistingContactEntity.photoFileId(contacts: Contacts): Long? =
    contacts.contentResolver.query(
        if (isProfile) ProfileUris.CONTACTS.uri else Table.Contacts.uri,
        Include(ContactsFields.PhotoFileId),
        ContactsFields.Id equalTo id
    ) {
        it.getNextOrNull { it.contactsCursor().photoFileId }
    }

private fun ExistingContactEntity.rawContactWithPhotoFileId(
    contacts: Contacts, photoFileId: Long
): TempRawContact? = contacts.contentResolver.query(
    if (isProfile) ProfileUris.DATA.uri else Table.Data.uri,
    Include(Fields.RawContact.Id),
    Fields.Photo.PhotoFileId equalTo photoFileId
) {
    it.getNextOrNull { it.dataCursor().tempRawContactMapper().value }
}

// endregion

// region REMOVE PHOTO

/**
 * Removes the photos, pending an update API call.
 *
 * If [fromAllRawContacts] is true, the photos of all RawContacts associated with this
 * [ExistingContactEntity] will be removed. Otherwise, only the photo of the
 * [ExistingContactEntity.primaryPhotoHolder] will be removed.
 *
 * The photo will not be removed until the update API call is committed successfully.
 *
 * If you want to directly remove the photo from the database, without an update API call, use
 * [MutableContact.removePhotoDirect].
 *
 * ## No includes required
 *
 * When using update APIs, there is is no field required to be passed into the `include` function
 * to make sure the photo is removed as long as this function is invoked.
 *
 * ## Not parcelable
 *
 * This action is ignored on parcel. This action will not be carried over across activities,
 * fragments, or views during creation/recreation.
 */
@JvmOverloads
fun MutableContact.removePhoto(fromAllRawContacts: Boolean = false) {
    if (fromAllRawContacts) {
        for (rawContact in rawContacts) {
            rawContact.photoDataOperation = PhotoDataOperation.RemovePhoto
        }
    } else {
        primaryPhotoHolder?.photoDataOperation = PhotoDataOperation.RemovePhoto
    }
}

// endregion

// region REMOVE PHOTO DIRECT

/**
 * Removes the photos directly from the database.
 *
 * If [fromAllRawContacts] is true, the photos of all RawContacts associated with this
 * [ExistingContactEntity] will be removed. Otherwise, only the photo of the
 * [ExistingContactEntity.primaryPhotoHolder] will be removed.
 *
 * Returns true if the operation succeeds.
 *
 * Supports profile and non-profile Contacts.
 *
 * ## Changes are immediate
 *
 * This function will make the changes to the Contacts Provider database immediately. You do not
 * need to use update APIs to commit the changes.
 *
 * ## Changes are not applied to the receiver
 *
 * This function call does NOT mutate immutable or mutable receivers. Therefore, you should use
 * query APIs or refresh extensions or process the result of this function call to get the most
 * up-to-date reference to mutable or immutable entity that contains the changes in the Contacts
 * Provider database.
 *
 * ## Permissions
 *
 * This requires the [ContactsPermissions.WRITE_PERMISSION].
 *
 * ## Thread Safety
 *
 * This should be called in a background thread to avoid blocking the UI thread.
 */
// [ANDROID X] @WorkerThread (not using annotation to avoid dependency on androidx.annotation)
@JvmOverloads
fun ExistingContactEntity.removePhotoDirect(
    contacts: Contacts, fromAllRawContacts: Boolean = false
): Boolean {
    if (!contacts.permissions.canUpdateDelete()) {
        return false
    }

    val whereId = if (fromAllRawContacts) {
        Fields.Contact.Id equalTo id
    } else {
        val primaryPhotoHolderId = primaryPhotoHolder?.id
        if (primaryPhotoHolderId != null) {
            Fields.RawContact.Id equalTo primaryPhotoHolderId
        } else {
            return false
        }
    }

    return contacts.contentResolver.applyBatch(
        newDelete(if (isProfile) ProfileUris.DATA.uri else Table.Data.uri)
            .withSelection(whereId and (Fields.MimeType equalTo MimeType.Photo))
            .build()
    ) != null
}

// endregion