package com.dsc.grocerymanagement.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.model.GroceryModel
import com.dsc.grocerymanagement.util.Utils

class CartRecyclerAdapter(
    private val context: Context,
    private val cartUpdate: CartUpdate
//    private val itemList: ArrayList<GroceryModel>
) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    interface CartUpdate {
        fun itemRemoved(item: GroceryModel)
    }

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.nametext)
        val save: TextView = itemView.findViewById(R.id.savetext)
        val price: TextView = itemView.findViewById(R.id.pricetext)
        val price0: TextView = itemView.findViewById(R.id.actualprice)
        val addBtn: Button = itemView.findViewById(R.id.add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view2 = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_listitem_single, parent, false)

        return CartViewHolder(view2)
    }

    override fun getItemCount(): Int {
        return Utils.getCart().size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val grocery = Utils.getCart()[position]
        if (Utils.checkCart(grocery))
            grocery.isAdded = true
        //println("grocey ${grocery.name}")
        holder.name.text = grocery.name
        holder.price.text = grocery.price
        holder.save.text = grocery.save
        holder.price0.text = grocery.price0
        holder.price0.paintFlags = holder.price0.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        if (grocery.isAdded) {
            holder.addBtn.text = "ADDED"
            holder.addBtn.setTextColor(context.getColor(R.color.red))
        } else {
//            itemList.remove(grocery)
            cartUpdate.itemRemoved(grocery)
            this.notifyItemRemoved(position)

        //            holder.addBtn.text = "ADD"
//            holder.addBtn.setTextColor(context.getColor(android.R.color.white))
        }
        holder.addBtn.setOnClickListener {
            grocery.isAdded = !grocery.isAdded
            Utils.updateCart(grocery)
//            if (!grocery.isAdded)
//                itemList.remove(grocery)
            cartUpdate.itemRemoved(grocery)
            this.notifyItemRemoved(position)
        }
        Glide.with(holder.img.context).load(grocery.img).into(holder.img)
    }

}