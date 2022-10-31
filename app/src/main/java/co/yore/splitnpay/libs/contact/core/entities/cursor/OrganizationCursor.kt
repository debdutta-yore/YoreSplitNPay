package co.yore.splitnpay.libs.contact.core.entities.cursor

import android.database.Cursor
import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.OrganizationField

/**
 * Retrieves [Fields.Organization] data from the given [cursor].
 *
 * This does not modify the [cursor] position. Moving the cursor may result in different attribute
 * values.
 */
internal class OrganizationCursor(cursor: Cursor, includeFields: Set<OrganizationField>) :
    AbstractDataCursor<OrganizationField>(cursor, includeFields) {

    val company: String? by string(Fields.Organization.Company)

    val title: String? by string(Fields.Organization.Title)

    val department: String? by string(Fields.Organization.Department)

    val jobDescription: String? by string(Fields.Organization.JobDescription)

    val officeLocation: String? by string(Fields.Organization.OfficeLocation)

    val symbol: String? by string(Fields.Organization.Symbol)

    val phoneticName: String? by string(Fields.Organization.PhoneticName)
}
