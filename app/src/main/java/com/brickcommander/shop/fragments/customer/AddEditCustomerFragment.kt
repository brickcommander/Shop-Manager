package com.brickcommander.shop.fragments.customer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.FragmentAddEditCustomerBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.shared.CONSTANTS
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.CustomerViewModel
import com.google.android.material.snackbar.Snackbar

class AddEditCustomerFragment : Fragment(R.layout.fragment_add_edit_customer) {
    companion object {
        const val TAG = "AddEditCustomerFragment"
    }

    private var _binding: FragmentAddEditCustomerBinding? = null
    private val binding get() = _binding!!

    private var currCustomer: Customer? = null
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var mView: View

    private lateinit var nameEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var dueAmountEditText: EditText
    private lateinit var customerNameSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditCustomerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currCustomer = arguments?.getParcelable("customer")
        Log.d(TAG, "onViewCreated: $currCustomer")
        customerViewModel = (activity as MainActivity).customerViewModel
        mView = view

        nameEditText = binding.nameEditText
        mobileEditText = binding.mobileEditText
        emailEditText = binding.emailEditText
        addressEditText = binding.addressEditText
        dueAmountEditText = binding.dueAmountEditText
        customerNameSpinner = binding.customerNameSpinner

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, CONSTANTS.NAME.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        customerNameSpinner.adapter = adapter

        currCustomer?.let {
            nameEditText.setText(it.name)
            mobileEditText.setText(it.mobile)
            emailEditText.setText(it.email)
            addressEditText.setText(it.address)
            dueAmountEditText.setText(it.dueAmount.toString())
            customerNameSpinner.setSelection(it.customerNameQ)
        }

        customerNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d(TAG, "customerNameSpinner Selected item: $selectedItem")
                currCustomer?.customerNameQ = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(TAG, "customerNameSpinner None Selected")
            }
        }
    }

    private fun saveItem(view: View) {
        if (nameEditText.text.toString().isEmpty()) {
            activity?.toast("Please Enter Item Name")
            return
        }

        var isNewItem = false
        if (currCustomer == null) {
            isNewItem = true
            currCustomer = Customer()
        }

        if (nameEditText.text.toString() != "") currCustomer!!.name = nameEditText.text.toString()
        if (mobileEditText.text.toString() != "") currCustomer!!.mobile = mobileEditText.text.toString()
        if (emailEditText.text.toString() != "") currCustomer!!.email = emailEditText.text.toString()
        if (addressEditText.text.toString() != "") currCustomer!!.address = addressEditText.text.toString()
        if (dueAmountEditText.text.toString() != "") currCustomer!!.dueAmount = dueAmountEditText.text.toString().trim().toDouble()
        if (customerNameSpinner.selectedItemPosition != 0) currCustomer!!.customerNameQ = customerNameSpinner.selectedItemPosition

        if (isNewItem) customerViewModel.add(currCustomer!!)
        else customerViewModel.update(currCustomer!!)

        Snackbar.make(
            view,
            "Note Saved successfully",
            Snackbar.LENGTH_SHORT
        ).show()

        if (isNewItem) view.findNavController().navigate(R.id.action_addEditCustomerFragment_to_homeCustomerFragment)
        else {
            val bundle = Bundle().apply {
                putParcelable("customer", currCustomer)
            }
            view.findNavController().navigate(R.id.action_addEditCustomerFragment_to_detailCustomerFragment, bundle)
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.save_menu -> {
                saveItem(mView)
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.new_note_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
