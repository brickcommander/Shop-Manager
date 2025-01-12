package com.brickcommander.shop.adapter.purchase.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.databinding.LayoutPurchaseAddEditItemAdapterBinding
import com.brickcommander.shop.model.Item
import com.brickcommander.shop.util.UnitsManager


class SearchItemAdapterForAddEditPurchase(
    private var items: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<SearchItemAdapterForAddEditPurchase.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: LayoutPurchaseAddEditItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.itemNameTextView.text = item.name
            binding.itemPriceTextView.text = item.sellingPrice.toString()
            binding.itemRemainingTextView.text = item.remainingCount.toString() + " " + UnitsManager.getNameById(item.remainingQ)

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutPurchaseAddEditItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}


