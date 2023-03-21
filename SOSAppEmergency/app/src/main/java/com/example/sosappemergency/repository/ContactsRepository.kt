package com.example.sosappemergency.repository

import androidx.lifecycle.LiveData
import com.example.sosappemergency.data.Contact
import com.example.sosappemergency.data.ContactsDao


class ContactsRepository(val dao: ContactsDao) {
    var allContacts:LiveData<List<Contact>> = dao.getAllContacts()

    suspend fun insert(contact: Contact){
        dao.insert(contact)
    }
    suspend fun delete(id: Int?){
        dao.delete(id)
    }
}