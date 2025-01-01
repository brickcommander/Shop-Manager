package com.brickcommander.shop.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.LayoutPurchaseListBinding
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.util.convertLongToFormattedDate

class PurchaseLiteAdapter : BaseAdapter<PurchaseLite, PurchaseLiteAdapter.PurchaseLiteViewHolder>() {

    class PurchaseLiteViewHolder(val binding: LayoutPurchaseListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseLiteViewHolder {
        return PurchaseLiteViewHolder(
            LayoutPurchaseListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PurchaseLiteViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        holder.binding.customerNameId.text = currentItem.customerName
        holder.binding.purchaseAmountId.text = currentItem.totalAmount.toString()
        holder.binding.purchaseDateId.text = convertLongToFormattedDate(currentItem.purchaseDate)

        val bundle = Bundle().apply {
            putParcelable("purchaseLite", currentItem)
        }
        holder.itemView.setOnClickListener { mView ->
            mView.findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment,
                bundle
            )
        }
    }

    override fun isSameItem(oldItem: PurchaseLite, newItem: PurchaseLite): Boolean {
        return oldItem.purchaseId == newItem.purchaseId
    }
}
