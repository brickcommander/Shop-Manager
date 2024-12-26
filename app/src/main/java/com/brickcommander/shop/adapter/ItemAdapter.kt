package com.brickcommander.shop.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.ItemLayoutListBinding
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.shared.CONSTANTS

class ItemAdapter : BaseAdapter<Item, ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val itemBinding: ItemLayoutListBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemLayoutListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        holder.itemBinding.nameId.text = currentItem.name
        holder.itemBinding.sellingPriceId.text = currentItem.sellingPrice.toString()

        if(currentItem.remainingQ > 0) {
            holder.itemBinding.remainingCountId.text = currentItem.remainingCount.toString() + CONSTANTS.QUANTITY[currentItem.remainingQ]
        } else {
            holder.itemBinding.remainingCountId.text = currentItem.remainingCount.toString()
        }

        val bundle = Bundle().apply {
            putParcelable("item", currentItem)
        }
        holder.itemView.setOnClickListener { mView ->
            mView.findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment,
                bundle
            )
        }
    }

    override fun isSameItem(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }
}
