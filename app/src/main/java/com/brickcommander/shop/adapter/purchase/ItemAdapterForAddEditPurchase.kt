package com.brickcommander.shop.adapter.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.databinding.LayoutPurchaseAddEditItemBinding
import com.brickcommander.shop.model.Item


class ItemAdapterForAddEditPurchase(
    private var items: List<Item>,
    private val removeItem: (Item) -> Unit,
    private val updateItem: (Item) -> Unit,
) : RecyclerView.Adapter<ItemAdapterForAddEditPurchase.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: LayoutPurchaseAddEditItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.itemNameTextView.text = item.name
            binding.itemPriceTextView.text = item.sellingPrice.toString()
            binding.itemQuantityTextView.text = item.remainingCount.toString()

            binding.editButton.setOnClickListener {
                updateItem(item)
            }

            binding.deleteButton.setOnClickListener {
                removeItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutPurchaseAddEditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


