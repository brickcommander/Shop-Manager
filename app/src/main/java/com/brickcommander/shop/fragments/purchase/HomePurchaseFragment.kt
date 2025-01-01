package com.brickcommander.shop.fragments.purchase

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.adapter.PurchaseLiteAdapter
import com.brickcommander.shop.fragments.BaseHomeFragment
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.viewModel.MyViewModel

class HomePurchaseFragment : BaseHomeFragment<PurchaseLite, PurchaseLiteAdapter.PurchaseLiteViewHolder>() {
    companion object {
        const val TAG = "PurchaseItemFragment"
    }

    override fun getViewModel(): MyViewModel<PurchaseLite> {
        return (activity as MainActivity).purchaseLiteViewModel
    }

    override fun setUpNavigation() {
        (requireActivity() as MainActivity).supportActionBar?.title = "Purchase History"

//        binding.fbAddItem.setOnClickListener { mView ->
//            mView.findNavController().navigate(R.id.action_homeFragment_to_addEditItemFragment)
//        }
//
//        binding.buttonCustomers.setOnClickListener { mView ->
//            mView.findNavController().navigate(R.id.action_homeFragment_to_homeCustomerFragment)
//        }
    }

    override fun setUpRecyclerView() {
        myAdapter = PurchaseLiteAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAdapter
        }

        activity?.let {
            myViewModel.getAll().observe(viewLifecycleOwner) { item ->
                Log.d(TAG, "Purchases: $item")
                myAdapter.differ.submitList(item)
                updateUI(item)
                Log.d(TAG, "setUpRecyclerView: ${myAdapter.itemCount}")
            }
        }
    }
}
