package com.brickcommander.shop.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.CustomerLayoutListBinding
import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.shared.CONSTANTS

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {
    class CustomerViewHolder(val customerBinding: CustomerLayoutListBinding) :
        RecyclerView.ViewHolder(customerBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<Customer>() {
            override fun areItemsTheSame(oldCustomer: Customer, newCustomer: Customer): Boolean {
                return oldCustomer.customerId == newCustomer.customerId
            }

            override fun areContentsTheSame(oldCustomer: Customer, newCustomer: Customer): Boolean {
                return oldCustomer == newCustomer
            }
        }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        return CustomerViewHolder(
            CustomerLayoutListBinding.inflate(
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
        holder.customerBinding.dueAmountId.text = currentCustomer.dueAmount.toString()

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

    override fun getItemCount() = differ.currentList.size
}