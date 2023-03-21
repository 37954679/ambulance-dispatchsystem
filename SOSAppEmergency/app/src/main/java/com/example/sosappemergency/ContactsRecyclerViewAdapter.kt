package com.example.sosappemergency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sosappemergency.data.Contact
import com.example.sosappemergency.databinding.ContactItemBinding


class ContactsDiffCallBack: DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return newItem == oldItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return newItem.equals(oldItem)
    }
}

class ContactsRecyclerViewAdapter(val clickListener: ClickListener):ListAdapter<Contact, ContactsRecyclerViewAdapter.ContactsViewHolder>(ContactsDiffCallBack()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ContactItemBinding.inflate(layoutInflater, parent, false)
            //val view = layoutInflater.inflate(R.layout.contact_item, parent, false)
            return ContactsViewHolder(binding)
        }

        override fun getItemId(position: Int): Long {
            return super.getItemId(position)
        }

        override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
            val item = getItem(position)
            return holder.bind(item,clickListener)


        }

        inner class ContactsViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item:Contact, clickListener: ClickListener ){
                binding.contact = item
                binding.clickListener = clickListener
                binding.executePendingBindings()
            }


        }


    }
class ClickListener(val clickListener: (contact: Contact)->Unit){
    fun onClick(contact:Contact) = clickListener(contact)
}
