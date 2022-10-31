package co.yore.splitnpay.libs.contact.core.entities.custom

import co.yore.splitnpay.libs.contact.core.ContactsException

/**
 * Exception thrown for any custom data errors.
 */
open class CustomDataException(message: String, cause: Throwable? = null) :
    ContactsException(message, cause)