package com.dsc.grocerymanagement.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.fragments.AboutAppPage
import com.dsc.grocerymanagement.fragments.CategoriesPage
import com.dsc.grocerymanagement.fragments.HomePage
import com.dsc.grocerymanagement.fragments.OffersPage
import com.dsc.grocerymanagement.util.IOnBackPressed
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class DashboardActivity : AppCompatActivity() {
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    lateinit var etPin: EditText
    private lateinit var btnPin: Button
    private lateinit var offer: ImageView
    private lateinit var txtPinResult: TextView
    private lateinit var pinResult: TextView
    private lateinit var img: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var auth: FirebaseAuth

    companion object {
        var collection: String = "as"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        drawerLayout = findViewById(R.id.DrawerLayout)
        navigationView = findViewById(R.id.NavigationView)
        //img = ImageView(this@DashboardActivity)
        txtPinResult = findViewById(R.id.txtPinResult)
        pinResult = findViewById(R.id.pinResult)
        txtPinResult.visibility = View.GONE
        offer = findViewById(R.id.offers)
        etPin = findViewById(R.id.etPin)
        etPin.visibility = View.VISIBLE
        pinResult.visibility = View.GONE
        btnPin = findViewById(R.id.btnPin)
        navigationView.menu.getItem(0).isChecked = true
        toolbar = findViewById(R.id.Toolbar)
//        etPin.hideKeyboard()
        auth = FirebaseAuth.getInstance()
        setUpToolbar()
        setMenuIcons(navigationView.menu)
        val header = navigationView.getHeaderView(0)
        val numberField: TextView = header.findViewById(R.id.txtMobile)
        numberField.text = auth.currentUser!!.phoneNumber.toString()
//        etPin.clearFocus()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@DashboardActivity,
            drawerLayout,
            R.string.Open_Drawer,
            R.string.Close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        openFragment(HomePage(), "All items", "grocery")
        /*img.setOnClickListener {
            openFragment(OffersPage(), "Offers", "null")
        }*/
        offer.setOnClickListener {
            openFragment(OffersPage(), "Offers", "null")
        }
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.hOme -> {
                    openFragment(HomePage(), "All items", "grocery")
                }
                R.id.spices -> {
                    openFragment(CategoriesPage(), "Spices", "spices")
                }
                R.id.pulses -> {
                    openFragment(CategoriesPage(), "Pulses", "Pulse")
                }
                /*R.id.rice -> {
                    openFragment(CategoriesPage(), "Rice", "Rice")
                }*/
                R.id.dairy -> {
                    openFragment(CategoriesPage(), "Dairy Products", "dairy")
                }
                R.id.hCare -> {
                    openFragment(CategoriesPage(), "Home Care", "homeCare")
                }
                R.id.bCare -> {
                    openFragment(CategoriesPage(), "Baby Care", "babyCare")
                }
                R.id.beverages -> {
                    openFragment(CategoriesPage(), "Beverages", "beverages")
                }
                R.id.pCare -> {
                    openFragment(CategoriesPage(), "Personal Care", "personal")
                }
                R.id.snacks -> {
                    openFragment(CategoriesPage(), "Snacks", "snacks")
                }
                R.id.noodles -> {
                    openFragment(CategoriesPage(), "Noodles", "noodles")
                }
                R.id.oil -> {
                    openFragment(CategoriesPage(), "Oil", "oil")
                }
                R.id.about -> {
                    openFragment(AboutAppPage(), "About App", "null")
                }
                R.id.logout -> {
                    logoutUser()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        etPin.addTextChangedListener(pinwatcher)

        btnPin.setOnClickListener {
            val pin = etPin.text.toString().trim()
            if (pin.length == 6) {
                etPin.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_pin,
                    0,
                    R.drawable.okay,
                    0
                )
                val result = "$pin confirmed<br><font color='#00FF00'>Change?</font>"
                pinResult.text = Html.fromHtml(result)
                txtPinResult.visibility = View.GONE
                etPin.visibility = View.GONE
                pinResult.visibility = View.VISIBLE
            } else {
                etPin.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_pin,
                    0,
                    R.drawable.cancel,
                    0
                )
                txtPinResult.visibility = View.VISIBLE
            }
        }
        pinResult.setOnClickListener {
            pinResult.visibility = View.GONE
            etPin.visibility = View.VISIBLE
            etPin.requestFocus()
            (etPin.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                0
            )
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.app_bar_cart -> startActivity(
                Intent(
                    this@DashboardActivity,
                    CartActivity::class.java
                )
            )//Log.d("Ayustark", "${Utils.getCart()}")
        }
        /*if (id == R.id.offers) {
            openFragment(OffersPage(), "Offers", "null")
        }*/
        return super.onOptionsItemSelected(item)
    }

    private fun openFragment(fragment: Fragment, title: String, collect: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.FrameLayout, fragment).commit()
        supportActionBar?.title = title
        collection = collect
        drawerLayout.closeDrawers()
    }

    private fun setMenuIcons(menu: Menu) {

        for (i in 1..10) {
            val imageView = ImageView(this@DashboardActivity)
            val item = menu.getItem(i)
            when (i) {
                1 -> {
                    imageView.setImageResource(R.mipmap.spice)
                }
                2 -> {
                    imageView.setImageResource(R.mipmap.pulses)
                }
                /*3 -> {
                    imageView.setImageResource(R.mipmap.rice)
                }*/
                3 -> {
                    imageView.setImageResource(R.mipmap.dairy)
                }
                4 -> {
                    imageView.setImageResource(R.mipmap.home)
                }
                5 -> {
                    imageView.setImageResource(R.mipmap.baby)
                }
                6 -> {
                    imageView.setImageResource(R.mipmap.bevarages)
                }
                7 -> {
                    imageView.setImageResource(R.mipmap.personal)
                }
                8 -> {
                    imageView.setImageResource(R.mipmap.snacks)
                }
                9 -> {
                    imageView.setImageResource(R.mipmap.noodles)
                }
                10 -> {
                    imageView.setImageResource(R.mipmap.oil)
                }
            }
            item.actionView = imageView
        }
    }

    private fun logoutUser() {
        val confirmLogout = AlertDialog.Builder(this@DashboardActivity)
        confirmLogout.setTitle("Sure to Logout??")
            .setMessage("You have to login again next time!!")
            .setPositiveButton("Confirm") { _, _ ->
                auth.signOut()
                startActivity(Intent(this@DashboardActivity, PhoneLoginActivity::class.java))
                Toast.makeText(
                    this@DashboardActivity, "You have been successfully Logged Out",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .setNegativeButton("Cancel") { _, _ ->
                navigationView.menu.getItem(0).isChecked = true
                openFragment(HomePage(), "All items", "grocery")
            }
            .create()
            .show()
    }

    fun getCollect(): String {
        return collection
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.FrameLayout)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            if (it) {

                when (supportFragmentManager.findFragmentById(R.id.FrameLayout)) {
                    !is HomePage -> {
                        openFragment(
                            HomePage(), "All items", "grocery"
                        )
                        navigationView.menu.getItem(0).isChecked = true
                    }
                    else -> {
                        super.onBackPressed()
                    }
                }
            }

        }
    }

    private val pinwatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //code to hide keyboard
            if (s?.length == 6) {
                btnPin.performClick()
                etPin.hideKeyboard()
            } else {
                etPin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pin, 0, 0, 0)
                //txtPinResult.visibility = View.VISIBLE

            }
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.offers, menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    fun EditText.hideKeyboard(): Boolean {
        return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            windowToken,
            0
        )
    }
}