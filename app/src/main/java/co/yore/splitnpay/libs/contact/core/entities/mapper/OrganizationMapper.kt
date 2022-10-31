package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Organization
import co.yore.splitnpay.libs.contact.core.entities.cursor.OrganizationCursor

internal class OrganizationMapper(private val organizationCursor: OrganizationCursor) :
    DataEntityMapper<Organization> {

    override val value: Organization
        get() = Organization(
            id = organizationCursor.dataId,
            rawContactId = organizationCursor.rawContactId,
            contactId = organizationCursor.contactId,

            isPrimary = organizationCursor.isPrimary,
            isSuperPrimary = organizationCursor.isSuperPrimary,

            company = organizationCursor.company,
            title = organizationCursor.title,
            department = organizationCursor.department,
            jobDescription = organizationCursor.jobDescription,
            officeLocation = organizationCursor.officeLocation,

            symbol = organizationCursor.symbol,
            phoneticName = organizationCursor.phoneticName,

            isRedacted = false
        )
}
