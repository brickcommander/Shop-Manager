package com.brickcommander.shop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.R
import com.brickcommander.shop.adapter.BaseAdapter
import com.brickcommander.shop.databinding.FragmentHomeBinding
import com.brickcommander.shop.viewModel.MyViewModel

abstract class BaseHomeFragment<T : Any, VH : RecyclerView.ViewHolder> : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {
    companion object {
        const val TAG = "BaseHomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    protected val binding get() = _binding!!

    protected lateinit var myViewModel: MyViewModel<T>
    protected lateinit var myAdapter: BaseAdapter<T, VH>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    abstract protected fun getViewModel(): MyViewModel<T>

    abstract protected fun setUpNavigation()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel = getViewModel()
        setUpRecyclerView()
        setUpNavigation()
    }

    abstract protected fun setUpRecyclerView()

    protected fun updateUI(note: List<T>) {
        if (note.isNotEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvNoNotesAvailable.visibility = View.GONE
            binding.cardView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.tvNoNotesAvailable.visibility = View.VISIBLE
            binding.cardView.visibility = View.VISIBLE
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
        if (query.isNullOrEmpty()) {
            myAdapter.differ.submitList(emptyList())
            return
        }
        val searchQuery = "%$query%"
        myViewModel.search(searchQuery).observe(this) { list ->
            myAdapter.differ.submitList(list)
        }
    }
}
