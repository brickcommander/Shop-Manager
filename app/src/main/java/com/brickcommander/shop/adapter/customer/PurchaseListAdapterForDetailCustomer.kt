package com.brickcommander.shop.adapter.customer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.navigation.findNavController
import com.brickcommander.shop.R
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.util.convertLongToFormattedDate

class PurchaseListAdapterForDetailCustomer(context: Context, private val items: List<PurchaseLite>) : ArrayAdapter<PurchaseLite>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_customer_info_recent_purchases_list, parent, false)

        val purchaseLite = items[position]

        val purchaseDateTextView = view.findViewById<TextView>(R.id.purchaseDateTextView)
        val purchaseAmountTextView = view.findViewById<TextView>(R.id.purchaseAmountTextView)

        purchaseDateTextView.text = convertLongToFormattedDate(purchaseLite.purchaseDate)
        purchaseAmountTextView.text = purchaseLite.totalAmount.toString() + " Rs"

        view.setOnClickListener { view ->
            val bundle = Bundle().apply {
                putParcelable("purchaseLite", purchaseLite)
            }
            view.findNavController().navigate(R.id.action_detailCustomerFragment_to_detailPurchaseFragment, bundle)
        }

        return view
    }
}