package com.brickcommander.shop.adapter.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.databinding.LayoutPurchaseAddEditItemBinding
import com.brickcommander.shop.model.helperModel.ItemDetail
import com.brickcommander.shop.util.UnitsManager
import com.brickcommander.shop.util.calculateAmount


class ItemAdapterForAddEditPurchase(
    private var itemsDetail: List<ItemDetail>,
    private var removeItem: (ItemDetail) -> Unit,
    private var updateItem: (ItemDetail) -> Unit,
) : RecyclerView.Adapter<ItemAdapterForAddEditPurchase.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: LayoutPurchaseAddEditItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemDetail: ItemDetail) {
            binding.itemNameTextView.text = itemDetail.item.name
            binding.itemPriceTextView.text = itemDetail.item.sellingPrice.toString() + " Rs/" + UnitsManager.getNameById(itemDetail.item.remainingQ)
            binding.itemQuantityTextView.text = itemDetail.quantity.toString() + " " + UnitsManager.getNameById(itemDetail.quantityQ)
            binding.itemTotalAmountTextView.text = "+" + calculateAmount(itemDetail).toString() + " Rs"

            binding.editButton.setOnClickListener {
                updateItem(itemDetail)
            }

            binding.deleteButton.setOnClickListener {
                removeItem(itemDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutPurchaseAddEditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsDetail[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemsDetail.size

    fun submitList(newItems: List<ItemDetail>) {
        itemsDetail = newItems
        notifyDataSetChanged()
    }
}


