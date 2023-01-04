package co.yore.splitnpay.libs.kontakts.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val name: String,
    @ColumnInfo val email: String,
    @ColumnInfo val image: String,
)