package co.yore.splitnpay.libs.contact.core.entities.operation

import android.content.ContentProviderOperation
import android.content.ContentProviderOperation.Builder
import co.yore.splitnpay.libs.contact.core.Field
import co.yore.splitnpay.libs.contact.core.Where
import co.yore.splitnpay.libs.contact.core.entities.table.Table

internal fun newInsert(table: Table<*>): Builder = ContentProviderOperation.newInsert(table.uri)

internal fun newUpdate(table: Table<*>): Builder = ContentProviderOperation.newUpdate(table.uri)

internal fun newDelete(table: Table<*>): Builder = ContentProviderOperation.newDelete(table.uri)

internal fun Builder.withSelection(where: Where<*>?): Builder =
    withSelection(where?.toString(), null)

internal fun Builder.withValue(field: Field, value: Any?): Builder =
    withValue(field.columnName, value)