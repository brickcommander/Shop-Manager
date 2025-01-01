package com.brickcommander.shop.fragments.purchase.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.adapter.purchase.search.SearchItemAdapterForAddEditPurchase
import com.brickcommander.shop.databinding.PopupSearchItemsBinding
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.viewModel.MyViewModel


class SearchItemsDialogFragment(private val onItemSelected: (Item) -> Unit) : DialogFragment() {

    private var _binding: PopupSearchItemsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel<Item>

    private val items: List<Item> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PopupSearchItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel = (activity as MainActivity).itemViewModel

        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SearchItemAdapterForAddEditPurchase(items) {
            onItemSelected(it)
            dismiss()
        }
        binding.recyclerViewSearch.adapter = adapter

        // Set up SearchView listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterItems(query, adapter)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterItems(newText, adapter)
                return true
            }
        })

        // Ensure the keyboard is shown and search bar gains focus
        binding.searchView.isFocusable = true
        binding.searchView.isIconified = false // Expand the search view
        binding.searchView.requestFocus()

        // Handle keyboard visibility adjustments
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keyboardHeight = screenHeight - rect.bottom

            if (keyboardHeight > screenHeight * 0.10) {
                // Keyboard is visible
                binding.recyclerViewSearch.setPadding(0, 0, 0, keyboardHeight)
            } else {
                // Keyboard is hidden
                binding.recyclerViewSearch.setPadding(0, 0, 0, 0)
            }
        }
    }


    private fun filterItems(query: String?, adapter: SearchItemAdapterForAddEditPurchase) {
        if (query.isNullOrEmpty()) {
            adapter.submitList(items)
            return
        }
        val searchQuery = "%$query%"
        myViewModel.search(searchQuery).observe(this) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
