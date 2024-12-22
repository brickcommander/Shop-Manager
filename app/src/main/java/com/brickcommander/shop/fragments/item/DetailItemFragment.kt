package com.brickcommander.shop.fragments.item

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
import com.brickcommander.shop.databinding.FragmentDetailsItemBinding
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.shared.CONSTANTS
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.ItemViewModel

class DetailItemFragment : Fragment(R.layout.fragment_details_item) {
    companion object {
        const val TAG = "DetailItemFragment"
    }

    private var _binding: FragmentDetailsItemBinding? = null
    private val binding get() = _binding!!

    private var currItem: Item? = null
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsItemBinding.inflate(
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
            view.findNavController().popBackStack(R.id.homeFragment, false) // Pop up to homeFragment
        }

        currItem = arguments?.getParcelable("item")
        Log.d(TAG, "onViewCreated: $currItem")
        itemViewModel = (activity as MainActivity).itemViewModel
        mView = view

        if(currItem == null) {
            activity?.toast("Error Occured!")
            view.findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        }

        binding.itemDetailsLayout2.nameId.text = currItem!!.name
        binding.itemDetailsLayout2.buyingPriceId.text = currItem!!.buyingPrice.toString() + " Rs"
        binding.itemDetailsLayout2.sellingPriceId.text = currItem!!.sellingPrice.toString() + " Rs"
        if(currItem!!.totalQ > 0) {
            binding.itemDetailsLayout2.totalCountId.text = currItem!!.totalCount.toString() + CONSTANTS.QUANTITY[currItem!!.totalQ]
        } else {
            binding.itemDetailsLayout2.totalCountId.text = currItem!!.totalCount.toString()
        }
        if(currItem!!.remainingQ > 0) {
            binding.itemDetailsLayout2.remainingCountId.text = currItem!!.remainingCount.toString() + CONSTANTS.QUANTITY[currItem!!.remainingQ]
        } else {
            binding.itemDetailsLayout2.remainingCountId.text = currItem!!.remainingCount.toString()
        }
    }

    private fun editItem(view: View) {
        val bundle = Bundle().apply {
            putParcelable("item", currItem)
        }
        view.findNavController().navigate(R.id.action_detailFragment_to_addEditItemFragment, bundle)
    }

    private fun deleteItem() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete ${currItem?.name}?")
            setPositiveButton("DELETE") { _, _ ->
                currItem?.let { itemViewModel.delete(it) }
                view?.findNavController()?.navigate(
                    R.id.action_detailFragment_to_homeFragment
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
