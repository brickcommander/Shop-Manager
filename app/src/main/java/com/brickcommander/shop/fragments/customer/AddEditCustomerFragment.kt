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
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.FragmentAddEditCustomerBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.shared.CONSTANTS
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.MyViewModel

class AddEditCustomerFragment : Fragment(R.layout.fragment_add_edit_customer) {
    companion object {
        const val TAG = "AddEditCustomerFragment"
    }

    private var _binding: FragmentAddEditCustomerBinding? = null
    private val binding get() = _binding!!

    private var currCustomer: Customer? = null
    private lateinit var customerViewModel: MyViewModel<Customer>
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

        if (currCustomer == null) (requireActivity() as MainActivity).supportActionBar?.title = "Add Customer"
        else (requireActivity() as MainActivity).supportActionBar?.title = "Update Customer"

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
        if (mobileEditText.text.toString().length != 10 && mobileEditText.text.toString().length != 12) {
            activity?.toast("Please Enter Valid Mobile Number")
            return
        }

        var isNewItem = false
        if (currCustomer == null) {
            isNewItem = true
            currCustomer = Customer()
        }

        if (nameEditText.text.toString() != "") currCustomer!!.name = nameEditText.text.toString().trim()
        if (mobileEditText.text.toString() != "") currCustomer!!.mobile = mobileEditText.text.toString().trim()
        if (emailEditText.text.toString() != "") currCustomer!!.email = emailEditText.text.toString().trim()
        if (addressEditText.text.toString() != "") currCustomer!!.address = addressEditText.text.toString().trim()
        if (dueAmountEditText.text.toString() != "") currCustomer!!.dueAmount = dueAmountEditText.text.toString().trim().toDouble()
        if (customerNameSpinner.selectedItemPosition != 0) currCustomer!!.customerNameQ = customerNameSpinner.selectedItemPosition

        if (isNewItem) customerViewModel.add(currCustomer!!)
        else customerViewModel.update(currCustomer!!)

        activity?.toast("Customer Saved Successfully")

        if (isNewItem) view.findNavController().navigate(R.id.action_addEditCustomerFragment_to_homeCustomerFragment)
        else {
            val bundle = Bundle().apply {
                putParcelable("customer", currCustomer)
            }
            view.findNavController().navigate(
                R.id.action_addEditCustomerFragment_to_detailCustomerFragment, bundle,
                NavOptions.Builder()
                    .setPopUpTo(R.id.detailCustomerFragment, true) // Pop up to FragmentX (inclusive)
                    .build()
            )
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
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
