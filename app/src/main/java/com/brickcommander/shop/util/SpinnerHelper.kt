package com.brickcommander.shop.util

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

object SpinnerHelper {
    fun setupItemSpinner(
        spinner: Spinner,
        items: List<String>,
        defaultPos: Int = 0,
        onUnitSelected: (Int) -> Unit
    ) {
        val adapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(defaultPos)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem: String = parent.getItemAtPosition(position).toString()
                UnitsManager.getUnitIdByName(selectedItem).let { id ->
                    onUnitSelected(id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No-op
            }
        }
    }

    fun setupCustomerSpinner(
        spinner: Spinner,
        items: List<String>,
        defaultId: Int = 0,
        onItemSelected: (Int) -> Unit
    ) {
        val adapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(defaultId)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                onItemSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No-op
            }
        }
    }
}