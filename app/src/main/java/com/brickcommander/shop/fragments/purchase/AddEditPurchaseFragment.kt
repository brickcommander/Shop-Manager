package com.example.app


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.brickcommander.shop.model.Item
import androidx.recyclerview.widget.LinearLayoutManager
import com.brickcommander.shop.R
import com.brickcommander.shop.adapter.purchase.ItemAdapterForAddEditPurchase
import com.brickcommander.shop.databinding.FragmentAddEditPurchaseBinding
import com.brickcommander.shop.fragments.purchase.search.SearchCustomersDialogFragment
import com.brickcommander.shop.fragments.purchase.search.SearchItemsDialogFragment
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.util.SpinnerHelper
import com.brickcommander.shop.util.getSpinnerListByCurrentQuantityType
import com.brickcommander.shop.util.toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AddEditPurchaseFragment : Fragment(R.layout.fragment_add_edit_purchase) {
    companion object {
        const val TAG = "AddEditPurchaseFragment"
    }
    private var _binding: FragmentAddEditPurchaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var mView: View

    private val selectedItems = mutableListOf<Item>()
    private var selectedCustomer: Customer? = null
    private var purchase: Purchase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditPurchaseBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        setupRecyclerView()

        binding.buttonAddItem.setOnClickListener {
            showItemSearchPopup { selectedItem ->
                lifecycleScope.launch {
                    val (quantity, quantityType) = showPopupAndWait(selectedItem.remainingQ)?: return@launch
                    handleSelectedItem(selectedItem, quantity, quantityType)
                    binding.recyclerViewItems.adapter?.notifyDataSetChanged()
                }
            }
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
        binding.recyclerViewItems.adapter = ItemAdapterForAddEditPurchase(
            selectedItems,
            removeItem = { itemToRemove ->
                removeItem(itemToRemove)
            },
            updateItem = { itemToUpdate ->
                updateItem(itemToUpdate)
            }
        )
    }

    private fun updateItem(itemToUpdate: Item) {
        lifecycleScope.launch {
            val (quantity, quantityType) = showPopupAndWait(itemToUpdate.remainingQ) ?: return@launch
            handleSelectedItem(itemToUpdate, quantity, quantityType)
            binding.recyclerViewItems.adapter?.notifyDataSetChanged() // Notify adapter to refresh
            activity?.toast("${itemToUpdate.name} update")
        }
    }

    private fun removeItem(itemToDelete: Item) {
        selectedItems.removeIf { it.itemId == itemToDelete.itemId }
        binding.recyclerViewItems.adapter?.notifyDataSetChanged() // Notify adapter to refresh
        activity?.toast("${itemToDelete.name} removed")
    }

    private fun showItemSearchPopup(onItemSelected: (Item) -> Unit) {
        val dialog = SearchItemsDialogFragment { selectedItem ->
            onItemSelected(selectedItem)
        }
        dialog.show(parentFragmentManager, "SearchItemsDialog")
    }

    private fun handleSelectedItem(selectedItem: Item, quantity: Double, quantityType: String) {
        Log.d(TAG, "Selected Item: $selectedItem, Quantity: $quantity, Quantity Type: $quantityType")
        selectedItem.remainingCount = quantity
        selectedItems.add(selectedItem)
    }

    private fun showCustomerSearchPopup() {
        val dialog = SearchCustomersDialogFragment { selectedCustomer ->
            this.selectedCustomer = selectedCustomer
            updateCustomer(selectedCustomer)
        }
        dialog.show(parentFragmentManager, "SearchCustomersDialog")
    }

    private suspend fun showPopupAndWait(selectedItemQ: Int): Pair<Double, String>? = suspendCancellableCoroutine { continuation ->
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_select_item_quantity, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val editText = dialogView.findViewById<EditText>(R.id.quantityEditText)
        val spinner = dialogView.findViewById<Spinner>(R.id.itemQuantitySpinner)
        val okButton = dialogView.findViewById<Button>(R.id.okButton)

        var itemQString = "None"
        var itemQuantity = 0.0

        SpinnerHelper.setupSpinner(
            context = requireContext(),
            spinner = spinner,
            items = getSpinnerListByCurrentQuantityType(selectedItemQ).toTypedArray(),
            defaultPosition = 0
        ) { _, selectedItem ->
            itemQString = selectedItem
        }

        okButton.setOnClickListener {
            val enteredText = editText.text.toString()
            itemQuantity = enteredText.toDoubleOrNull() ?: 0.0
            alertDialog.dismiss()

            // Resume coroutine with the result
            continuation.resume(Pair(itemQuantity, itemQString))
        }

        alertDialog.setOnDismissListener {
            // If dialog is dismissed without action, resume with default values
            if (continuation.isActive) continuation.resume(null)
        }

        alertDialog.show()
    }

    private fun addItemToList(selectedItem: Item, quantity: Double, quantityType: String) {
        selectedItems.add(selectedItem)
        binding.recyclerViewItems.adapter?.notifyDataSetChanged()
        Log.d(TAG, "addItemToList: Item added - $selectedItem")
    }

    private fun saveItem(view: View) {
        if(selectedCustomer == null) {
            activity?.toast("Please Select the Customer")
            return
        } else if(selectedItems.size == 0) {
            activity?.toast("Please Select the Items")
            return
        } else if(purchase == null) {
            activity?.toast("Error Occured!")
            return
        }


//        if (nameEditText.text.toString().isEmpty()) {
//            activity?.toast("Please Enter Item Name")
//            return
//        }
//
//        var isNewItem = false
//        if (currItem == null) {
//            isNewItem = true
//            currItem = Item()
//        }
//
//        if (nameEditText.text.toString() != "") currItem!!.name = nameEditText.text.toString().trim()
//        if (buyEditText.text.toString() != "") currItem!!.buyingPrice = buyEditText.text.toString().trim().toDouble()
//        if (sellEditText.text.toString() != "") currItem!!.sellingPrice = sellEditText.text.toString().trim().toDouble()
//        if (totalEditText.text.toString() != "") currItem!!.totalCount = totalEditText.text.toString().trim().toDouble()
//        if (remainingEditText.text.toString() != "") currItem!!.remainingCount = remainingEditText.text.toString().trim().toDouble()
//        if (itemTotalSpinner.selectedItemPosition != 0) currItem!!.totalQ = itemTotalSpinner.selectedItemPosition
//        if (itemRemSpinner.selectedItemPosition != 0) currItem!!.remainingQ = itemRemSpinner.selectedItemPosition
//
//        if (isNewItem) itemViewModel.add(currItem!!)
//        else itemViewModel.update(currItem!!)
//
//        activity?.toast("Item Saved successfully")
//
//        if (isNewItem) view.findNavController().navigate(R.id.action_addEditItemFragment_to_homeFragment)
//        else {
//            val bundle = Bundle().apply {
//                putParcelable("item", currItem)
//            }
//            view.findNavController().navigate(R.id.action_addEditItemFragment_to_detailFragment, bundle)
//        }
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
        menu.clear()
        inflater.inflate(R.menu.new_note_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}