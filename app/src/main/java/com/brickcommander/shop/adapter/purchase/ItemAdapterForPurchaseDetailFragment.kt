package com.brickcommander.shop.adapter.purchase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.brickcommander.shop.R
import com.brickcommander.shop.model.helperModel.ItemDetail

class ItemAdapterForPurchaseDetailFragment(context: Context, private val items: List<ItemDetail>) : ArrayAdapter<ItemDetail>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_purchase_info_item, parent, false)

        val itemDetail = items[position]

        val itemNameTextView = view.findViewById<TextView>(R.id.itemNameTextView)
        val itemPriceTextView = view.findViewById<TextView>(R.id.itemPriceTextView)
        val itemQuantityTextView = view.findViewById<TextView>(R.id.itemQuantityTextView)

        itemNameTextView.text = itemDetail.item.name
        itemPriceTextView.text = itemDetail.sellingPrice.toString()
        itemQuantityTextView.text = itemDetail.quantity.toString()

        return view
    }
}