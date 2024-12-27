package com.brickcommander.shop.fragments.item

import android.util.Log
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.adapter.ItemAdapter
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.fragments.BaseHomeFragment
import com.brickcommander.shop.viewModel.MyViewModel

class HomeItemFragment : BaseHomeFragment<Item, ItemAdapter.ItemViewHolder>() {
    companion object {
        const val TAG = "HomeItemFragment"
    }

    override fun getViewModel(): MyViewModel<Item> {
        return (activity as MainActivity).itemViewModel
    }

    override fun setUpNavigation() {
        (requireActivity() as MainActivity).supportActionBar?.title = "SHOP"

        binding.fbAddItem.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeFragment_to_addEditItemFragment)
        }

        binding.buttonCustomers.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeFragment_to_homeCustomerFragment)
        }
    }

    override fun setUpRecyclerView() {
        myAdapter = ItemAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAdapter
        }

        activity?.let {
            myViewModel.getAll().observe(viewLifecycleOwner) { item ->
                Log.d(TAG, "Items: $item")
                myAdapter.differ.submitList(item)
                updateUI(item)
                Log.d(TAG, "setUpRecyclerView: ${myAdapter.itemCount}")
            }
        }
    }
}
