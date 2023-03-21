package com.example.sosappemergency.viewmodels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sosappemergency.data.Contact
import com.example.sosappemergency.data.ContactsDataBase
import com.example.sosappemergency.repository.ContactsRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageContactsViewModel(application: Application):AndroidViewModel(application) {
    val allContacts: LiveData<List<Contact>>

        val userLocation: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }

    fun setLocation(location: Location){
        Log.d("LOCATION", "called setLocation method")
        Log.d("LOCATION", "called setLocation method with: ${location}")

        userLocation.value = location
    }

    private val repository: ContactsRepository

    init {
        val contactsDao =
            ContactsDataBase.getInstance(application.applicationContext, viewModelScope)
                .contactsDao()
        repository = ContactsRepository(contactsDao)
        Log.d("SOSAppEmergency", "In viewmodel init")
        allContacts = repository.allContacts
    }

    suspend fun insert(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(contact)
        }
    }

    suspend fun delete(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }
}

