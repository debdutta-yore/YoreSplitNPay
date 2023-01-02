package co.yore.splitnpay.libs.kontakts

import androidx.room.*


@Dao
interface ContactEntityDao {
    @Query("SELECT * FROM ContactEntity")
    fun getAll(): List<ContactEntity>

    @Insert
    fun insertAll(vararg users: ContactEntity)

    @Insert
    fun insertAll(users: List<ContactEntity>)

    @Update
    fun updateAll(users: List<ContactEntity>)

    @Update
    fun updateAll(vararg users: ContactEntity)

    @Update
    fun update(users: ContactEntity)

    @Delete
    fun delete(user: ContactEntity)

    @Delete
    fun delete(users: List<ContactEntity>)

    @Query("DELETE FROM ContactEntity WHERE id = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM ContactEntity WHERE id in (:ids)")
    fun deleteByIds(ids: List<String>)
}