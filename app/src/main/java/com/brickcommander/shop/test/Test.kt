package com.brickcommander.shop.test

import com.brickcommander.shop.model.Customer
import com.brickcommander.shop.model.Item

class Test {
    companion object {
        val TAG = "Test"

        fun generateItems(n: Int): List<Item> {
            val items = mutableListOf<Item>()
            for (i in 1..n) {
                items.add(
                    Item(
                        "Item ${i}",
                        i.toDouble(),
                        1,
                        i.toDouble() + 10.0,
                        1,
                        (i % 100).toDouble(),
                        1
                    )
                )
            }
            return items
        }

        fun generateCustomers(n: Int): List<Customer> {
            val customers = mutableListOf<Customer>()
            for (i in 1..n) {
                customers.add(
                    Customer(
                        "Customer ${i}",
                        "1234567890",
                        "william.henry.harrison${i}@example-pet-store.com",
                        "Address Vars Jacaranda Doddanakundhi PIN 560037 - ${i}",
                        0
                    )
                )
            }
            return customers
        }
    }
}