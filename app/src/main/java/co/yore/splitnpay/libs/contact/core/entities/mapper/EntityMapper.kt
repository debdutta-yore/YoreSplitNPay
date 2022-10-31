package co.yore.splitnpay.libs.contact.core.entities.mapper

import co.yore.splitnpay.libs.contact.core.entities.ExistingCustomDataEntity
import co.yore.splitnpay.libs.contact.core.entities.ExistingDataEntity
import co.yore.splitnpay.libs.contact.core.entities.ExistingEntity

/**
 * Creates models for existing entities from database values.
 */
internal sealed interface EntityMapper<out T : ExistingEntity> {
    val value: T

    /**
     * The [value] if it is not blank. Else, returns null.
     */
    val nonBlankValueOrNull: T?
        get() = if (value.isBlank) null else value
}

/**
 * Creates models for existing data entities from database values.
 */
internal sealed interface DataEntityMapper<out T : ExistingDataEntity> : EntityMapper<T>

/**
 * Creates models for existing custom data entities from database values.
 */
// Intentionally not sealed.
internal interface CustomDataEntityMapper<out T : ExistingCustomDataEntity> : DataEntityMapper<T>