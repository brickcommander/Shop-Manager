package com.brickcommander.shop.fragments.customer

import android.util.Log
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.adapter.CustomerAdapter
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.fragments.BaseHomeFragment
import com.brickcommander.shop.viewModel.MyViewModel

class HomeCustomerFragment : BaseHomeFragment<Customer, CustomerAdapter.CustomerViewHolder>() {
    companion object {
        const val TAG = "HomeCustomerFragment"
    }

    override fun getViewModel(): MyViewModel<Customer> {
        return (activity as MainActivity).customerViewModel
    }

    override fun setUpNavigation() {
        (requireActivity() as MainActivity).supportActionBar?.title = "Customers"

        binding.fbAddItem.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeCustomerFragment_to_addEditCustomerFragment)
        }

        binding.buttonItems.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeCustomerFragment_to_homeFragment)
        }
    }

    override fun setUpRecyclerView() {
        myAdapter = CustomerAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAdapter
        }


        activity?.let {
            myViewModel.getAll().observe(viewLifecycleOwner) { customer ->
                Log.d(TAG, "Customers: $customer")
                myAdapter.differ.submitList(customer)
                updateUI(customer)
                Log.d(TAG, "setUpRecyclerView: ${myAdapter.itemCount}")
            }
        }

    }
}
