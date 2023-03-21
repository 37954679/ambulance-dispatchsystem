package com.example.sosappemergency

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepakkumardk.kontactpickerlib.KontactPicker
import com.deepakkumardk.kontactpickerlib.model.KontactPickerItem
import com.example.sosappemergency.data.Contact
import com.example.sosappemergency.databinding.FragmentManageContactsBinding
import com.example.sosappemergency.viewmodels.ManageContactsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_CONTACT_PICK_CODE = 3000

class ManageContactsFragment : Fragment() {
    private lateinit var contactsFragmentBinding: FragmentManageContactsBinding
    private lateinit var viewModel: ManageContactsViewModel

    private lateinit var localList: List<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        contactsFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_manage_contacts, container, false )
        viewModel = ViewModelProvider(this).get(ManageContactsViewModel::class.java)
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = ContactsRecyclerViewAdapter(
            ClickListener {
                lifecycleScope.launch {
                    viewModel.delete(it.id)

                }

            }
        )
        contactsFragmentBinding.contactsList.adapter = adapter
        contactsFragmentBinding.contactsList.layoutManager = layoutManager

        contactsFragmentBinding.floatingActionButtonAddContact.setOnClickListener {
            addContacts()
        }


        viewModel.allContacts.observe(viewLifecycleOwner, Observer { contactsList->
            contactsList?.let {it->
                Log.d("EXCITE", "contactsList:${it}")
                adapter.submitList(it)
                localList = it
            }
            if (localList.isNotEmpty()){
                contactsFragmentBinding.textView.visibility = View.GONE
            }
        })
        contactsFragmentBinding.lifecycleOwner = this

        //contactsFragmentBinding.contactsList
        return contactsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    fun addContacts(){
        KontactPicker().startPickerForResult(this, KontactPickerItem(), 3000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CONTACT_PICK_CODE){
            val list = KontactPicker.getSelectedKontacts(data)
            for (item in list!!.iterator()){
                //add them to database?

                Log.d("HEZCONTACT", "${item.contactName}")
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO){
                        viewModel.insert(Contact(item.contactName, item.contactNumber, item.contactId?.toInt()))
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageContactsFragment().apply {

            }
    }
}