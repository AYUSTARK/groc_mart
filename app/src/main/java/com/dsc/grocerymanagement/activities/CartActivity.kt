package com.dsc.grocerymanagement.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.adapter.CartRecyclerAdapter
import com.dsc.grocerymanagement.model.GroceryModel
import com.dsc.grocerymanagement.model.RazorLink
import com.dsc.grocerymanagement.network.ApiHelper
import com.dsc.grocerymanagement.network.ApiManager
import com.dsc.grocerymanagement.util.Resource
import com.dsc.grocerymanagement.util.Status
import com.dsc.grocerymanagement.util.Utils
import com.dsc.grocerymanagement.util.Utils.Companion.getCart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CartActivity : AppCompatActivity() {
    private lateinit var recview: RecyclerView
    private lateinit var recyclerAdapter: CartRecyclerAdapter
    private lateinit var count: TextView
    private lateinit var total: TextView
    private lateinit var delivery: TextView
    private lateinit var gst: TextView
    private lateinit var price: TextView
    private lateinit var save: TextView
    private lateinit var payBtn: Button
    private var mUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initialize()
    }

    private fun initialize() {
        recview = findViewById(R.id.cartList)
        count = findViewById(R.id.count)
        total = findViewById(R.id.total)
        delivery = findViewById(R.id.delivery)
        gst = findViewById(R.id.gst)
        price = findViewById(R.id.price)
        save = findViewById(R.id.save)
        payBtn = findViewById(R.id.pay)
        mUser = FirebaseAuth.getInstance().currentUser
        recyclerAdapter =
            CartRecyclerAdapter(this@CartActivity, object : CartRecyclerAdapter.CartUpdate {
                override fun itemRemoved(item: GroceryModel) {
                    calculateSummary()
                }

            })
        recview.layoutManager = LinearLayoutManager(this@CartActivity)
        recview.adapter = recyclerAdapter
        calculateSummary()
    }

    private fun calculateSummary() {
        if (getCart().isEmpty())
            return
        val itemTotal = calculateItemTotal()
        val deliveryPrice = 10.00
        val tax = (itemTotal + deliveryPrice) * 18 / 100
        val totalPrice = itemTotal + deliveryPrice + tax
        val saved = calculateTotalSave()
        payBtn.setOnClickListener {
            getLink(totalPrice)
        }
        Log.d("Ayustark", "$itemTotal $tax $totalPrice")
        count.text = getCart().size.toString()
        total.text = "Rs. $itemTotal"
        delivery.text = "Rs. $deliveryPrice"
        gst.text = "Rs. $tax"
        price.text = "Rs. $totalPrice"
        save.text = "Rs. $saved"
    }

    private fun calculateTotalSave(): Double {
        var totalSave = 0.0
        getCart().forEach {
            if (it.save.length > 2)
                totalSave += it.save.substring(it.save.indexOf("₹") + 1).toFloat()
        }
        return totalSave
    }

    private fun calculateItemTotal(): Double {
        var totalPrice = 0.0
        getCart().forEach {
            if (it.price.length > 2)
                totalPrice += it.price.substring(it.price.indexOf("₹") + 2).toFloat()
        }
        return totalPrice
    }

    private fun getLink(amount: Double) {
        try {
            payBtn.visibility = View.INVISIBLE
            val payment = MutableLiveData<Resource<RazorLink>>()
            if (mUser?.phoneNumber == null)
                return
            ApiHelper(ApiManager).getPaymentLink(mUser?.phoneNumber!!.substring(3), amount, payment)
            payment.observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        Log.d("Payment", "${it.data?.status}")
                        if (it.data?.status == 200) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(it.data.response?.shortUrl)
                                )
                            )
                            Utils.clearCart()
                            finish()
                        } else {
                            payBtn.visibility = View.VISIBLE
                            Toast.makeText(
                                this@CartActivity,
                                "${it.data?.message}  ${it.data?.status}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.ERROR -> {
                        payBtn.visibility = View.VISIBLE
                        Log.e("Payment", "${it.data}")
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Log.e("Payment", "${it.data}")
                    }
                }
            }
        } catch (err: Exception) {
            payBtn.visibility = View.VISIBLE
            Toast.makeText(this@CartActivity, err.message, Toast.LENGTH_LONG).show()
            Log.e("Register get Link", "${err.message} ${err.cause}")
        }
    }
}