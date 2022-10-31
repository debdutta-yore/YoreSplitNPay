package co.yore.splitnpay.libs.contact.core.entities.operation

import android.content.ContentProviderOperation
import co.yore.splitnpay.libs.contact.core.GroupsFields
import co.yore.splitnpay.libs.contact.core.entities.ExistingGroupEntity
import co.yore.splitnpay.libs.contact.core.entities.NewGroup
import co.yore.splitnpay.libs.contact.core.entities.table.Table
import co.yore.splitnpay.libs.contact.core.equalTo

private val TABLE = Table.Groups

/**
 * Builds [ContentProviderOperation]s for [Table.Groups].
 */
internal class GroupsOperation {

    fun insert(group: NewGroup): ContentProviderOperation = newInsert(TABLE)
        .withValue(GroupsFields.Title, group.title)
        .withValue(GroupsFields.AccountName, group.account.name)
        .withValue(GroupsFields.AccountType, group.account.type)
        // Setting favorites and auto add has no effect. The Contacts Provider will routinely set
        // them to false for all user-created groups.
        // .withValue(Fields.Group.Favorites, it.favorites.toSqlValue())
        // .withValue(Fields.Group.AutoAdd, it.autoAdd.toSqlValue())
        .build()

    fun update(group: ExistingGroupEntity): ContentProviderOperation = newUpdate(TABLE)
        .withSelection(GroupsFields.Id equalTo group.id)
        .withValue(GroupsFields.Title, group.title)
        .build()
}