package com.brickcommander.shop.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.LayoutCustomerListBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.shared.CONSTANTS

class CustomerAdapter : BaseAdapter<Customer, CustomerAdapter.CustomerViewHolder>() {

    class CustomerViewHolder(val customerBinding: LayoutCustomerListBinding) : RecyclerView.ViewHolder(customerBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        return CustomerViewHolder(
            LayoutCustomerListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val currentCustomer = differ.currentList[position]

        if(currentCustomer.customerNameQ > 0) {
            holder.customerBinding.nameId.text = CONSTANTS.NAME[currentCustomer.customerNameQ] + " " + currentCustomer.name
        } else {
            holder.customerBinding.nameId.text = currentCustomer.name
        }
        holder.customerBinding.contactNumberId.text = currentCustomer.mobile
        holder.customerBinding.dueAmountId.text = currentCustomer.dueAmount.toString() + " Rs"

        val bundle = Bundle().apply {
            putParcelable("customer", currentCustomer)
        }
        holder.itemView.setOnClickListener { mView ->
            mView.findNavController().navigate(
                R.id.action_homeCustomerFragment_to_detailCustomerFragment,
                bundle
            )
        }
    }

    override fun isSameItem(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem.customerId == newItem.customerId
    }
}
