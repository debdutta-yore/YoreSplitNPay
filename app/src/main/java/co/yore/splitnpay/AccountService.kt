package co.yore.splitnpay

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val accountIdKey = stringPreferencesKey("accountId")
val accountImage = stringPreferencesKey("accountImage")
val accountName = stringPreferencesKey("accountName")
val Context.accountDataStore: DataStore<Preferences> by preferencesDataStore(name = "accountPreference")

class AccountService @Inject constructor(
    val context: Context
) {

    /*suspend fun setContactFetchedTimestamp(
        context: Context,
        millis: Long
    ) {
        context.contactDataStore.edit { settings ->
            settings[contactFetchedTimestamp] = millis
        }
    }*/

    suspend fun getAccountId(): String {
        return context.accountDataStore.data.map {
            it[accountIdKey]?:"+918967114927"
        }.first()
    }

    suspend fun getAccountImage(): String {
        return context.accountDataStore.data.map {
            it[accountImage]?:"name://${getAccountName()}"
        }.first()
    }

    suspend fun getAccountName(): String {
        return context.accountDataStore.data.map {
            it[accountName]?:"Debdutta Panda"
        }.first()
    }
}