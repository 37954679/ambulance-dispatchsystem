package com.example.sosappemergency.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [Contact::class],
    version = 1, exportSchema = false
)

abstract class ContactsDataBase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao

    companion object {
        @Volatile
        var INSTANCE: ContactsDataBase? = null

        fun getInstance(context: Context, scope:CoroutineScope): ContactsDataBase {
            val tempInstance = INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context, ContactsDataBase::class.java, "contacts_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}