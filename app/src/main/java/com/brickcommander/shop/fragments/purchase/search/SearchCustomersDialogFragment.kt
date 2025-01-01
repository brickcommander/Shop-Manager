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
import com.brickcommander.shop.adapter.purchase.SearchCustomerAdapterForAddEditPurchase
import com.brickcommander.shop.databinding.PopupSearchItemsBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.viewModel.MyViewModel


class SearchCustomersDialogFragment(private val onItemSelected: (Customer) -> Unit) : DialogFragment() {

    private var _binding: PopupSearchItemsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel<Customer>

    private val items = listOf(
        Customer("Customer 1", "10"),
        Customer("Customer 2", "20"),
        Customer("Customer 3", "30"),
        Customer("Customer 4", "40"),
        Customer("Customer 5", "50"),
        Customer("Customer 6", "60"),
        Customer("Customer 7", "70"),
        Customer("Customer 8", "80"),
        Customer("Customer 9", "90"),
        Customer("Customer 10", "100")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PopupSearchItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel = (activity as MainActivity).customerViewModel

        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SearchCustomerAdapterForAddEditPurchase(items) {
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
                binding.recyclerViewSearch.setPadding(0, 0, 0, keyboardHeight+120)
            } else {
                // Keyboard is hidden
                binding.recyclerViewSearch.setPadding(0, 0, 0, 120)
            }
        }
    }


    private fun filterItems(query: String?, adapter: SearchCustomerAdapterForAddEditPurchase) {
        val searchQuery = "%$query%"
        myViewModel.search(searchQuery).observe(this) { list ->
            adapter.submitList(items)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
