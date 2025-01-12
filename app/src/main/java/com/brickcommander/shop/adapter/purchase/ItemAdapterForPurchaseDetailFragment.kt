package com.brickcommander.shop.adapter.purchase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.brickcommander.shop.R
import com.brickcommander.shop.model.helperModel.ItemDetail
import com.brickcommander.shop.util.UnitsManager
import com.brickcommander.shop.util.calculateAmount

class ItemAdapterForPurchaseDetailFragment(context: Context, private val items: List<ItemDetail>) : ArrayAdapter<ItemDetail>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_purchase_info_item, parent, false)

        val itemDetail = items[position]

        val itemNameTextView = view.findViewById<TextView>(R.id.itemNameTextView)
        val itemPriceTextView = view.findViewById<TextView>(R.id.itemPriceTextView)
        val itemQuantityTextView = view.findViewById<TextView>(R.id.itemQuantityTextView)
        val itemTotalAmountTextView = view.findViewById<TextView>(R.id.itemTotalAmountTextView)

        itemNameTextView.text = itemDetail.item.name
        itemTotalAmountTextView.text = "+" + calculateAmount(itemDetail).toString() + " Rs"
        itemPriceTextView.text = itemDetail.sellingPrice.toString() + " Rs/" + UnitsManager.getNameById(itemDetail.item.sellingQ)
        itemQuantityTextView.text = itemDetail.quantity.toString() + " " + UnitsManager.getNameById(itemDetail.quantityQ)

        return view
    }
}