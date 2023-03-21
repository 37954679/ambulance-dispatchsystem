package com.example.sosappemergency.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
class Contact(var contactName:String? = "Contact Name", var phoneNumber:String? = "Phone Number",  @PrimaryKey var id:Int?=null ) {


}