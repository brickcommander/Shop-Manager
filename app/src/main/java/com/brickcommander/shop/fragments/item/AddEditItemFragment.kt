package com.brickcommander.shop.fragments.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.FragmentAddEditItemTempBinding
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.util.SpinnerHelper
import com.brickcommander.shop.util.UnitsManager
import com.brickcommander.shop.util.findPositionFromUnitId
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.MyViewModel

class AddEditItemFragment : Fragment(R.layout.fragment_add_edit_item_temp) {
    companion object {
        const val TAG = "AddEditItemFragment"
    }

    private var _binding: FragmentAddEditItemTempBinding? = null
    private val binding get() = _binding!!

    private var isNewItem: Boolean = false
    private lateinit var currItem: Item
    private lateinit var itemViewModel: MyViewModel<Item>
    private lateinit var mView: View

    private lateinit var nameEditText: EditText
    private lateinit var buyEditText: EditText
    private lateinit var buyingUnitSpinner: Spinner
    private lateinit var sellEditText: EditText
    private lateinit var sellingUnitSpinner: Spinner
    private lateinit var remainingEditText: EditText
    private lateinit var remUnitSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditItemTempBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemViewModel = (activity as MainActivity).itemViewModel
        mView = view

        nameEditText = binding.nameEditText
        buyEditText = binding.buyEditText
        buyingUnitSpinner = binding.itemBuyingUnitSpinner
        sellEditText = binding.sellEditText
        sellingUnitSpinner = binding.itemSellingUnitSpinner
        remainingEditText = binding.remainingEditText
        remUnitSpinner = binding.remUnitSpinner

        val argItem: Item? = arguments?.getParcelable("item")
        if (argItem == null) {
            (requireActivity() as MainActivity).supportActionBar?.title = "Add Item"

            isNewItem = true
            currItem = Item()
            val unitNames = UnitsManager.getUnitNamesByUnitType(-1)

            SpinnerHelper.setupItemSpinner(buyingUnitSpinner, unitNames, 0) { selectedUnitId ->
                Log.d(TAG, "onViewCreated: buyingUnitSpinner: $selectedUnitId")
                currItem.buyingQ = selectedUnitId
            }

            SpinnerHelper.setupItemSpinner(sellingUnitSpinner, unitNames, 0) { selectedUnitId ->
                Log.d(TAG, "onViewCreated: sellingUnitSpinner: $selectedUnitId")
                currItem.sellingQ = selectedUnitId
            }

            SpinnerHelper.setupItemSpinner(remUnitSpinner, unitNames, 0) { selectedUnitId ->
                Log.d(TAG, "onViewCreated: itemRemSpinner: $selectedUnitId")
                currItem.remainingQ = selectedUnitId
            }
        } else {
            (requireActivity() as MainActivity).supportActionBar?.title = "Update Item"

            currItem = argItem
            val unitNames = UnitsManager.getUnitNamesByUnitType(currItem.sellingQ)

            SpinnerHelper.setupItemSpinner(buyingUnitSpinner, unitNames, findPositionFromUnitId(unitNames, currItem.buyingQ)) { selectedUnitId ->
                Log.d(TAG, "onViewCreated: buyingUnitSpinner: $selectedUnitId")
                currItem.buyingQ = selectedUnitId
            }

            SpinnerHelper.setupItemSpinner(sellingUnitSpinner, unitNames, findPositionFromUnitId(unitNames, currItem.sellingQ)) { selectedUnitId ->
                Log.d(TAG, "onViewCreated: sellingUnitSpinner: $selectedUnitId")
                currItem.sellingQ = selectedUnitId
            }

            SpinnerHelper.setupItemSpinner(remUnitSpinner, unitNames, findPositionFromUnitId(unitNames, currItem.remainingQ)) { selectedUnitId ->
                Log.d(TAG, "onViewCreated: itemRemSpinner: $selectedUnitId")
                currItem.remainingQ = selectedUnitId
            }

            nameEditText.setText(currItem!!.name)
            buyEditText.setText(currItem!!.buyingPrice.toString())
            sellEditText.setText(currItem!!.sellingPrice.toString())
            remainingEditText.setText(currItem!!.remainingCount.toString())
        }
    }

    private fun saveItem(view: View) {
        if (nameEditText.text.toString().isEmpty()) {
            activity?.toast("Please Enter Item Name")
            return
        }
        if (sellEditText.text.toString().isEmpty() || sellEditText.text.toString().trim().toDouble() <= 0.0) {
            activity?.toast("Please Select Valid Selling Price")
            return
        }
        if (buyEditText.text.toString().isEmpty() || buyEditText.text.toString().trim().toDouble() <= 0.0) {
            activity?.toast("Please Select Valid Buying Price")
            return
        }
        if (!UnitsManager.hasSameUnitType(currItem.buyingQ, currItem.sellingQ, currItem.remainingQ)) {
            activity?.toast("Please Select Valid Units")
            return
        }

        currItem!!.name = nameEditText.text.toString().trim()
        currItem!!.buyingPrice = buyEditText.text.toString().trim().toDouble()
        currItem!!.sellingPrice = sellEditText.text.toString().trim().toDouble()
        if (remainingEditText.text.toString() != "") currItem!!.remainingCount = remainingEditText.text.toString().trim().toDouble()
//        if (itemTotalSpinner.selectedItemPosition != 0) currItem!!.totalQ = itemTotalSpinner.selectedItemPosition

        if (isNewItem) itemViewModel.add(currItem!!)
        else itemViewModel.update(currItem!!)

        activity?.toast("Item Saved successfully")

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
        Log.d(TAG, "onCreateOptionsMenu: ")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
