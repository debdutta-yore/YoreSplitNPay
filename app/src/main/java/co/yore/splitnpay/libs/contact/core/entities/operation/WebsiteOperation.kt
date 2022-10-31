package co.yore.splitnpay.libs.contact.core.entities.operation

import co.yore.splitnpay.libs.contact.core.Fields
import co.yore.splitnpay.libs.contact.core.WebsiteField
import co.yore.splitnpay.libs.contact.core.entities.MimeType
import co.yore.splitnpay.libs.contact.core.entities.WebsiteEntity

internal class WebsiteOperation(isProfile: Boolean, includeFields: Set<WebsiteField>) :
    AbstractDataOperation<WebsiteField, WebsiteEntity>(isProfile, includeFields) {

    override val mimeType = MimeType.Website

    override fun setValuesFromData(
        data: WebsiteEntity, setValue: (field: WebsiteField, dataValue: Any?) -> Unit
    ) {
        setValue(Fields.Website.Url, data.url)
    }
}