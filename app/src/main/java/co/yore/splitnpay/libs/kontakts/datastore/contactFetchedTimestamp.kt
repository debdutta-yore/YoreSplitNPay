package co.yore.splitnpay.libs.kontakts.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val contactFetchedTimestamp = longPreferencesKey("contactFetchedTimestamp")
val contactsSkipped = intPreferencesKey("contactsSkipped")
val Context.contactDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun setContactFetchedTimestamp(
    context: Context,
    millis: Long
) {
    context.contactDataStore.edit { settings ->
        settings[contactFetchedTimestamp] = millis
    }
}

suspend fun getContactFetchedTimestamp(
    context: Context
): Long {
    return context.contactDataStore.data.map {
        it[contactFetchedTimestamp] ?: 0
    }.first()
}

//////////
suspend fun setContactSkippedCount(
    context: Context,
    count: Int
) {
    context.contactDataStore.edit { settings ->
        settings[contactsSkipped] = count
    }
}

suspend fun getContactSkippedCount(
    context: Context
): Int {
    return context.contactDataStore.data.map {
        it[contactsSkipped] ?: 0
    }.first()
}