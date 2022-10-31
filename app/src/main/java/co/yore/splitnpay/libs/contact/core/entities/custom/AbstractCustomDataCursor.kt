package co.yore.splitnpay.libs.contact.core.entities.custom

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.AbstractDataField
import co.yore.splitnpay.libs.contact.core.entities.cursor.AbstractDataCursor
import co.yore.splitnpay.libs.contact.core.entities.cursor.DataCursor

/**
 * Base type of all custom [DataCursor]s. The type [T] are the fields allowed to be used in the
 * functions defined in [contacts.core.entities.cursor.AbstractEntityCursor].
 */
abstract class AbstractCustomDataCursor<T : AbstractDataField>(
    cursor: Cursor, includeFields: Set<T>
) : AbstractDataCursor<T>(cursor, includeFields)