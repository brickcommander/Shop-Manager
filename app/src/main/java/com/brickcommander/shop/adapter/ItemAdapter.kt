package com.brickcommander.shop.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.ItemLayoutListBinding
import com.brickcommander.shop.model.Item
import java.util.Random

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(val itemBinding: ItemLayoutListBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
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

//        holder.itemBinding.tvItemTitle.text = currentItem.noteTitle
//        holder.itemBinding.tvItemBody.text = currentItem.noteBody

        holder.itemView.setOnClickListener { mView ->
            mView.findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment
            )
        }
    }

    override fun getItemCount() = differ.currentList.size
}