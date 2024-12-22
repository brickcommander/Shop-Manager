package com.brickcommander.shop.fragments.customer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.adapter.CustomerAdapter
import com.brickcommander.shop.viewModel.CustomerViewModel
import com.brickcommander.shop.databinding.FragmentHomeItemBinding
import com.brickcommander.shop.model.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeCustomerFragment : Fragment(R.layout.fragment_home_item), SearchView.OnQueryTextListener {
    companion object {
        const val TAG = "HomeCustomerFragment"
    }

    private var _binding: FragmentHomeItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var customerAdapter: CustomerAdapter

    private var animationJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeItemBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerViewModel = (activity as MainActivity).customerViewModel
        setUpRecyclerView()
        binding.fbAddItem.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeCustomerFragment_to_addEditCustomerFragment)
        }

        binding.buttonItems.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeCustomerFragment_to_homeFragment)
        }
    }

    private fun setUpRecyclerView() {
        customerAdapter = CustomerAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = customerAdapter
        }

        activity?.let {
            customerViewModel.getAll().observe(viewLifecycleOwner) { customer ->
                Log.d(TAG, "Customers: $customer")
                customerAdapter.differ.submitList(customer)
                updateUI(customer)
            }
        }
    }

    private fun updateUI(note: List<Customer>) {
        if (note.isNotEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvNoNotesAvailable.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.tvNoNotesAvailable.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)

        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchItem(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchItem(newText)
        }
        return true
    }

    private fun searchItem(query: String?) {
        val searchQuery = "%$query%"
        customerViewModel.search(searchQuery).observe(this) { list ->
            customerAdapter.differ.submitList(list)
        }
    }

    private fun startLoadingAnimation() {
        val baseText = "Loading"
        val dots = listOf("", ".", "..", "...")
        var index = 0

        binding.loadingTextView.visibility = View.VISIBLE
        animationJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                binding.loadingTextView.text = baseText + dots[index]
                index = (index + 1) % dots.size
                delay(500) // Delay between each update
            }
        }
    }

    private fun stopLoadingAnimation() {
        animationJob?.cancel()
        binding.loadingTextView.visibility = View.GONE
    }

}