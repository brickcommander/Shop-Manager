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
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.adapter.purchase.ItemAdapterForAddEditPurchase
import com.brickcommander.shop.databinding.FragmentAddEditPurchaseBinding
import com.brickcommander.shop.fragments.purchase.search.SearchCustomersDialogFragment
import com.brickcommander.shop.fragments.purchase.search.SearchItemsDialogFragment
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.model.helperModel.ItemDetail
import com.brickcommander.shop.util.SpinnerHelper
import com.brickcommander.shop.util.getItemQFromItemQString
import com.brickcommander.shop.util.getSpinnerListByCurrentQuantityType
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.PurchaseViewModel
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
    private lateinit var purchaseViewModel: PurchaseViewModel

    private val selectedItems = mutableListOf<ItemDetail>()
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
        purchaseViewModel = (activity as MainActivity).purchaseViewModel
        mView = view

        setupRecyclerView()

        binding.buttonAddItem.setOnClickListener {
            showItemSearchPopup { selectedItem ->
                lifecycleScope.launch {
                    val (quantity, quantityQ) = showPopupAndWait(selectedItem.remainingQ)?: return@launch
                    var itemDetail = ItemDetail(selectedItem, quantity, quantityQ)
                    handleSelectedItem(itemDetail)
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
            removeItem = { itemDetailToRemove ->
                removeItem(itemDetailToRemove)
            },
            updateItem = { itemDetailToUpdate ->
                updateItem(itemDetailToUpdate)
            }
        )
    }

    private fun updateItem(itemDetail: ItemDetail) {
        lifecycleScope.launch {
            val (quantity, quantityQ) = showPopupAndWait(itemDetail.quantityQ) ?: return@launch
            itemDetail.quantity = quantity
            itemDetail.quantityQ = quantityQ
            handleSelectedItem(itemDetail)
            activity?.toast("${itemDetail.item.name} updated")
            binding.recyclerViewItems.adapter?.notifyDataSetChanged() // Notify adapter to refresh
        }
    }

    private fun removeItem(itemDetail: ItemDetail) {
        selectedItems.removeIf { it.item.itemId == itemDetail.item.itemId }
        activity?.toast("${itemDetail.item.name} removed")
        binding.recyclerViewItems.adapter?.notifyDataSetChanged() // Notify adapter to refresh
    }

    private fun showItemSearchPopup(onItemSelected: (Item) -> Unit) {
        val dialog = SearchItemsDialogFragment { selectedItem ->
            onItemSelected(selectedItem)
        }
        dialog.show(parentFragmentManager, "SearchItemsDialog")
    }

    private fun handleSelectedItem(itemDetail: ItemDetail) {
        Log.d(TAG, "Selected Item: ${itemDetail.item}, Quantity: ${itemDetail.quantity}, Quantity Type: ${itemDetail.quantityQ}")
        selectedItems.add(itemDetail)
    }

    private fun showCustomerSearchPopup() {
        val dialog = SearchCustomersDialogFragment { selectedCustomer ->
            this.selectedCustomer = selectedCustomer
            updateCustomer(selectedCustomer)
        }
        dialog.show(parentFragmentManager, "SearchCustomersDialog")
    }

    private suspend fun showPopupAndWait(selectedItemDetailQ: Int): Pair<Double, Int>? = suspendCancellableCoroutine { continuation ->
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_select_item_quantity, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val editText = dialogView.findViewById<EditText>(R.id.quantityEditText)
        val spinner = dialogView.findViewById<Spinner>(R.id.itemQuantitySpinner)
        val okButton = dialogView.findViewById<Button>(R.id.okButton)

        var itemQ = 0
        var itemQuantity = 0.0

        SpinnerHelper.setupSpinner(
            context = requireContext(),
            spinner = spinner,
            items = getSpinnerListByCurrentQuantityType(selectedItemDetailQ).toTypedArray(),
            defaultPosition = 0
        ) { _, selectedItem ->
            itemQ = getItemQFromItemQString(selectedItem)
        }

        okButton.setOnClickListener {
            val enteredText = editText.text.toString()
            itemQuantity = enteredText.toDoubleOrNull() ?: 0.0
            alertDialog.dismiss()

            // Resume coroutine with the result
            if(itemQuantity == 0.0) {
                context?.toast("Quantity not valid.")
            } else {
                continuation.resume(Pair(itemQuantity, itemQ))
            }
        }

        alertDialog.setOnDismissListener {
            // If dialog is dismissed without action, resume with default values
            if (continuation.isActive) continuation.resume(null)
        }

        alertDialog.show()
    }

    private fun addItemToList(itemDetail: ItemDetail) {
        selectedItems.add(itemDetail)
        binding.recyclerViewItems.adapter?.notifyDataSetChanged()
        Log.d(TAG, "addItemToList: Item added - $itemDetail")
    }

    private fun saveItem(view: View, activePurchase: Boolean = false) {
        if(selectedCustomer == null) {
            if(activePurchase) {
                return
            } else {
                activity?.toast("Please Select Customer")
                return
            }
        } else if(selectedItems.size == 0) {
            if(activePurchase) {
                return
            } else {
                activity?.toast("Please Select Items")
                return
            }
        } else if(purchase == null) {
            purchase = Purchase(
                items = selectedItems,
                customer = selectedCustomer!!,
                active = activePurchase,
                purchaseDate = System.currentTimeMillis(),
                totalAmount = 0.0,
                purchaseId = 0
            )
            purchaseViewModel.add(purchase!!)
            activity?.toast("Purchase Saved successfully")
//            view.findNavController().navigate(R.id.action_addEditPurchaseFragment_to_homeFragment)
        } else {
            purchase!!.items = selectedItems
            purchase!!.customer = selectedCustomer!!
            purchase!!.purchaseDate = System.currentTimeMillis()
            purchase!!.active = activePurchase
            purchaseViewModel.update(purchase!!)
            activity?.toast("Purchase Updated successfully")
        }

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
        saveItem(mView, true)
    }
}