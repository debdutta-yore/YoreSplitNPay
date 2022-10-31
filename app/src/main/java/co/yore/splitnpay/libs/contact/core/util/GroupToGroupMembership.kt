package co.yore.splitnpay.libs.contact.core.util

import co.yore.splitnpay.libs.contact.core.entities.ExistingGroupEntity
import co.yore.splitnpay.libs.contact.core.entities.NewGroupMembership

/**
 * Returns a new [NewGroupMembership] instance that may be used for Contacts and RawContacts insert
 * and update operations.
 */
fun ExistingGroupEntity.newMembership() = NewGroupMembership(groupId = id, isRedacted = isRedacted)

/**
 * Returns [this] collection of [ExistingGroupEntity]s as list of [NewGroupMembership] that may be
 * used for Contacts and RawContacts insert and update operations.
 */
fun Collection<ExistingGroupEntity>.newMemberships(): List<NewGroupMembership> =
    map { it.newMembership() }

/**
 * Returns [this] sequence of [ExistingGroupEntity]s as list of [NewGroupMembership] that may be
 * used for Contacts and RawContacts insert and update operations.
 */
fun Sequence<ExistingGroupEntity>.newMemberships(): Sequence<NewGroupMembership> =
    map { it.newMembership() }