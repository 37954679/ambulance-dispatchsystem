package com.example.sosappemergency.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.sosappemergency.data.Contact

@Dao
interface ContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

//    @Delete
//    suspend fun delete(contact: Contact){
//
//    }
    @Query("DELETE FROM contacts WHERE id= :contact_id")
    fun delete(contact_id:Int?):Unit;
    @Query("SELECT * FROM contacts")
    fun getAllContacts():LiveData<List<Contact>>

}