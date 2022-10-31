package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.Options
import co.yore.splitnpay.libs.contact.core.entities.cursor.OptionsCursor

/**
 * Mapper for Contacts or RawContacts options from the Data, RawContacts, or Contacts table.
 */
internal class OptionsMapper(private val optionsCursor: OptionsCursor<*>) : EntityMapper<Options> {

    override val value: Options
        get() = Options(
            id = optionsCursor.id,

            starred = optionsCursor.starred,

            // Deprecated in API 29 - contains useless value for all Android versions in Play store.
            // timesContacted = optionsCursor.timesContacted,
            // lastTimeContacted = optionsCursor.lastTimeContacted,

            customRingtone = optionsCursor.customRingtone,

            sendToVoicemail = optionsCursor.sendToVoicemail,

            isRedacted = false
        )
}
