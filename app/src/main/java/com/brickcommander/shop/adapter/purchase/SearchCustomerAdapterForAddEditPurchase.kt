package com.brickcommander.shop.adapter.purchase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.databinding.LayoutCustomerListBinding
import com.brickcommander.shop.model.Customer

class SearchCustomerAdapterForAddEditPurchase(
    private var customers: List<Customer>,
    private val onItemClick: (Customer) -> Unit
) : RecyclerView.Adapter<SearchCustomerAdapterForAddEditPurchase.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: LayoutCustomerListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: Customer) {
            binding.nameId.text = customer.name
            binding.contactNumberId.text = customer.mobile
            binding.dueAmountId.text = customer.dueAmount.toString()

            itemView.setOnClickListener {
                onItemClick(customer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutCustomerListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val customer = customers[position]
        holder.bind(customer)
    }

    override fun getItemCount(): Int = customers.size

    fun submitList(newCustomers: List<Customer>) {
        customers = newCustomers
        notifyDataSetChanged()
    }
}


