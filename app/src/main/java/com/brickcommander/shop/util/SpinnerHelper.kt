package com.brickcommander.shop.util

import android.R
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

object SpinnerHelper {
    fun setupSpinner(
        context: Context,
        spinner: Spinner,
        items: Array<String>,
        defaultPosition: Int = 0,
        onItemSelected: (Int, String) -> Unit
    ) {
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(defaultPosition)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                onItemSelected(position, selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No-op
            }
        }
    }
}