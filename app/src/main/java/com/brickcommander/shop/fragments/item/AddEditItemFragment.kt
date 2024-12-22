package com.brickcommander.shop.fragments.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.FragmentAddEditItemBinding
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.shared.CONSTANTS
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.ItemViewModel
import com.google.android.material.snackbar.Snackbar

class AddEditItemFragment : Fragment(R.layout.fragment_add_edit_item) {
    companion object {
        const val TAG = "AddEditItemFragment"
    }

    private var _binding: FragmentAddEditItemBinding? = null
    private val binding get() = _binding!!

    private var currItem: Item? = null
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var mView: View

    private lateinit var nameEditText: EditText
    private lateinit var buyEditText: EditText
    private lateinit var sellEditText: EditText
    private lateinit var totalEditText: EditText
    private lateinit var remainingEditText: EditText
    private lateinit var itemTotalSpinner: Spinner
    private lateinit var itemRemSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditItemBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currItem = arguments?.getParcelable("item")
        Log.d(TAG, "onViewCreated: $currItem")
        itemViewModel = (activity as MainActivity).itemViewModel
        mView = view

        nameEditText = binding.nameEditText
        buyEditText = binding.buyEditText
        sellEditText = binding.sellEditText
        totalEditText = binding.totalEditText
        remainingEditText = binding.remainingEditText
        itemTotalSpinner = binding.itemTotalSpinner
        itemRemSpinner = binding.itemRemSpinner

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, CONSTANTS.QUANTITY.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemTotalSpinner.adapter = adapter
        itemRemSpinner.adapter = adapter

        currItem?.let {
            nameEditText.setText(it.name)
            buyEditText.setText(it.buyingPrice.toString())
            sellEditText.setText(it.sellingPrice.toString())
            totalEditText.setText(it.totalCount.toString())
            remainingEditText.setText(it.remainingCount.toString())
            itemTotalSpinner.setSelection(it.totalQ)
            itemRemSpinner.setSelection(it.remainingQ)
        }

        itemTotalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d(TAG, "itemTotalSpinner Selected item: $selectedItem")
                currItem?.totalQ = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(TAG, "itemTotalSpinner None Selected")
            }
        }

        itemRemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d(TAG, "itemRemSpinner Selected item: $selectedItem")
                currItem?.remainingQ = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(TAG, "itemRemSpinner None Selected")
            }
        }
    }

    private fun saveItem(view: View) {
        if (nameEditText.text.toString().isEmpty()) {
            activity?.toast("Please Enter Item Name")
            return
        }

        var isNewItem = false
        if (currItem == null) {
            isNewItem = true
            currItem = Item()
        }

        if (nameEditText.text.toString() != "") currItem!!.name = nameEditText.text.toString()
        if (buyEditText.text.toString() != "") currItem!!.buyingPrice = buyEditText.text.toString().toDouble()
        if (sellEditText.text.toString() != "") currItem!!.sellingPrice = sellEditText.text.toString().toDouble()
        if (totalEditText.text.toString() != "") currItem!!.totalCount = totalEditText.text.toString().toInt()
        if (remainingEditText.text.toString() != "") currItem!!.remainingCount = remainingEditText.text.toString().toInt()
        if (itemTotalSpinner.selectedItemPosition != 0) currItem!!.totalQ = itemTotalSpinner.selectedItemPosition
        if (itemRemSpinner.selectedItemPosition != 0) currItem!!.remainingQ = itemRemSpinner.selectedItemPosition

        if (isNewItem) itemViewModel.add(currItem!!)
        else itemViewModel.update(currItem!!)

        Snackbar.make(
            view,
            "Note Saved successfully",
            Snackbar.LENGTH_SHORT
        ).show()

        if (isNewItem) view.findNavController().navigate(R.id.action_addEditItemFragment_to_homeFragment)
        else {
            val bundle = Bundle().apply {
                putParcelable("item", currItem)
            }
            view.findNavController().navigate(R.id.action_addEditItemFragment_to_detailFragment, bundle)
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.save_menu -> {
                saveItem(mView)
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.new_note_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
