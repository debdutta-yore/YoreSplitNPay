package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.OrganizationField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.OrganizationEntity

internal class OrganizationOperation(isProfile: Boolean, includeFields: Set<OrganizationField>) :
    AbstractDataOperation<OrganizationField, OrganizationEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Organization

    override fun setValuesFromData(
        data: OrganizationEntity, setValue: (field: OrganizationField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Organization.Company, data.company)
        setValue(Fields.Organization.Title, data.title)
        setValue(Fields.Organization.Department, data.department)
        setValue(Fields.Organization.JobDescription, data.jobDescription)
        setValue(Fields.Organization.OfficeLocation, data.officeLocation)

        setValue(Fields.Organization.Symbol, data.symbol)
        setValue(Fields.Organization.PhoneticName, data.phoneticName)
    }
}