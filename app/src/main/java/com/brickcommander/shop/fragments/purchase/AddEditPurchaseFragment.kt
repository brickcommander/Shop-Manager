package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.brickcommander.shop.model.Item


import androidx.recyclerview.widget.LinearLayoutManager
import com.brickcommander.shop.adapter.purchase.ItemAdapterForAddEditPurchase
import com.brickcommander.shop.databinding.FragmentAddEditPurchaseBinding
import com.brickcommander.shop.fragments.purchase.search.SearchCustomersDialogFragment
import com.brickcommander.shop.fragments.purchase.search.SearchItemsDialogFragment
import com.brickcommander.shop.model.Customer

class AddEditPurchaseFragment : Fragment() {

    private var _binding: FragmentAddEditPurchaseBinding? = null
    private val binding get() = _binding!!
    private val selectedItems = mutableListOf<Item>()
    private var selectedCustomer: Customer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.buttonAddItem.setOnClickListener {
            showItemSearchPopup()
        }

        binding.buttonAddCustomer.setOnClickListener {
            showCustomerSearchPopup()
        }
    }

    private fun updateCustomer(customer: Customer) {
        binding.customerDetailsLayout.customerNameTextView.text = customer.name
        binding.customerDetailsLayout.customerMobileTextView.text = customer.mobile
    }

    private fun setupRecyclerView() {
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewItems.adapter = ItemAdapterForAddEditPurchase(selectedItems) {
            Toast.makeText(requireContext(), "${it.name} selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showItemSearchPopup() {
        val dialog = SearchItemsDialogFragment { selectedItem ->
            selectedItems.add(selectedItem)
            binding.recyclerViewItems.adapter?.notifyDataSetChanged()
        }
        dialog.show(parentFragmentManager, "SearchItemsDialog")
    }

    private fun showCustomerSearchPopup() {
        val dialog = SearchCustomersDialogFragment { selectedCustomer ->
            this.selectedCustomer = selectedCustomer
            updateCustomer(selectedCustomer)
        }
        dialog.show(parentFragmentManager, "SearchCustomersDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}