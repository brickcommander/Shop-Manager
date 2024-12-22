package com.brickcommander.shop.fragments.customer

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.FragmentDetailsCustomerBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.shared.CONSTANTS
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.CustomerViewModel

class DetailCustomerFragment : Fragment(R.layout.fragment_details_customer) {
    companion object {
        const val TAG = "DetailCustomerFragment"
    }

    private var _binding: FragmentDetailsCustomerBinding? = null
    private val binding get() = _binding!!

    private var currCustomer: Customer? = null
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsCustomerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Log.d(TAG, "onBackPressedDispatcher: ")
            view.findNavController().popBackStack(R.id.homeCustomerFragment, false) // Pop up to homeFragment
        }

        currCustomer = arguments?.getParcelable("customer")
        Log.d(TAG, "onViewCreated: $currCustomer")
        customerViewModel = (activity as MainActivity).customerViewModel
        mView = view

        if(currCustomer == null) {
            activity?.toast("Error Occured!")
            view.findNavController().navigate(R.id.action_detailCustomerFragment_to_homeCustomerFragment)
        }

        if(currCustomer!!.customerNameQ > 0) {
            binding.customerDetailsLayout.customerNameId.text = CONSTANTS.NAME[currCustomer!!.customerNameQ] + " " + currCustomer!!.name
        } else {
            binding.customerDetailsLayout.customerNameId.text = currCustomer!!.name
        }
        binding.customerDetailsLayout.mobileId.text = currCustomer!!.mobile
        binding.customerDetailsLayout.emailId.text = currCustomer!!.email
        binding.customerDetailsLayout.addressId.text = currCustomer!!.address
        binding.customerDetailsLayout.dueAmountId.text = currCustomer!!.dueAmount.toString()
    }

    private fun editItem(view: View) {
        val bundle = Bundle().apply {
            putParcelable("customer", currCustomer)
        }
        view.findNavController().navigate(R.id.action_detailCustomerFragment_to_addEditCustomerFragment, bundle)
    }

    private fun deleteItem() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete ${currCustomer?.name}?")
            setPositiveButton("DELETE") { _, _ ->
                currCustomer?.let { customerViewModel.delete(it) }
                view?.findNavController()?.navigate(
                    R.id.action_detailCustomerFragment_to_homeCustomerFragment
                )
            }
            setNegativeButton("CANCEL", null)
        }.create().show()
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.update_menu -> {
                editItem(mView)
            }
            R.id.delete_menu -> {
                deleteItem()
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_note, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}