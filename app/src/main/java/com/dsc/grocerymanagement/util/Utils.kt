package com.dsc.grocerymanagement.util

import android.util.Log
import com.dsc.grocerymanagement.model.GroceryModel

class Utils() {
    companion object {
        private val itemsinCart = arrayListOf<GroceryModel>()
        fun updateCart(item: GroceryModel) {
            if (item.isAdded && !checkCart(item)) {
                itemsinCart.add(item)
            } else {
                Log.d("Ayustark",itemsinCart.remove(item).toString())
            }
        }

        fun checkCart(item: GroceryModel): Boolean {
            if (itemsinCart.has(item))
                return true
            return false
        }

        fun getCart(): ArrayList<GroceryModel> {
            return itemsinCart
        }

        private fun ArrayList<GroceryModel>.has(item: GroceryModel): Boolean {
            for (i in this) {
                if (i == item)
                    return true
            }
            return false
        }

        fun clearCart() {
            itemsinCart.clear()
        }
    }
}