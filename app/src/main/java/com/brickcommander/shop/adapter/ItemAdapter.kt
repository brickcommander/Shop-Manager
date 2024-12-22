package com.brickcommander.shop.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.ItemLayoutListBinding
import com.brickcommander.shop.model.Item

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(val itemBinding: ItemLayoutListBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemId == newItem.itemId
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, differCallback)

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
        holder.itemBinding.remainingCountId.text = currentItem.remainingCount.toString()

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

    override fun getItemCount() = differ.currentList.size
}
