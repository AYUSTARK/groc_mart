package com.dsc.grocerymanagement.fragments

import android.app.SearchManager
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.adapter.HomeRecyclerAdapter
import com.dsc.grocerymanagement.model.GroceryModel
import com.dsc.grocerymanagement.util.IOnBackPressed
import com.dsc.grocerymanagement.util.Utils.Companion.updateCart
import com.dsc.grocerymanagement.util.Utils.Companion.checkCart
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class HomePage : Fragment(), IOnBackPressed {
    private lateinit var recview: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var search: SearchView
    lateinit var progressLayout: RelativeLayout
    private lateinit var recyclerAdapter: HomeRecyclerAdapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var size = 0
    private var searchFlag = 1
    private val groceryArrayList = arrayListOf<GroceryModel>()
    private lateinit var adapter: FirestoreRecyclerAdapter<*, *>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_items_page, container, false)
        setHasOptionsMenu(true)
        progressLayout = view.findViewById(R.id.progressLayout)
        search = view.findViewById(R.id.search)
        search.isIconified = false
        search.clearFocus()
        recview = view.findViewById(R.id.firestore_list)
        progressLayout.visibility = View.VISIBLE
        search()
        firebaseFirestore = FirebaseFirestore.getInstance()
        //Query
        firebaseFirestore.collection("grocery").get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {

            } else {
                val types: List<GroceryModel> = querySnapshot.toObjects(
                    GroceryModel::class.java)
                // Add all to your list

                groceryArrayList.addAll(types)
                size = groceryArrayList.size
            }

        }.addOnFailureListener {
            Toast.makeText(activity as Context, "Error getting data!!!", Toast.LENGTH_SHORT).show()
        }
        val query: Query = FirebaseFirestore.getInstance()
                .collection("grocery")
        /*Handler().postDelayed({
            println("size $size")
            recyclerAdapter=HomeRecyclerAdapter(groceryArrayList)
            recview.layoutManager = LinearLayoutManager(activity as Context)
            recview.adapter = recyclerAdapter
            recyclerAdapter.notifyDataSetChanged()
        }, 3000)*/
        recview.setHasFixedSize(true)
        getList(query)

        // Inflate the layout for this fragment
        return view
    }

    private fun getList(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<GroceryModel>()
                .setQuery(query, GroceryModel::class.java)
                .build()
        adapter = object : FirestoreRecyclerAdapter<GroceryModel, GroceryViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
                val view2 = LayoutInflater.from(parent.context).inflate(R.layout.activity_listitem_single, parent, false)
                return GroceryViewHolder(view2)
            }

            override fun onBindViewHolder(holder: GroceryViewHolder, position: Int, model: GroceryModel) {
                holder.container.animation = AnimationUtils.loadAnimation(activity as Context, R.anim.fade)
                if (checkCart(model))
                    model.isAdded = true
                holder.name.text = model.name
                holder.price.text = model.price
                holder.save.text = model.save
                holder.price0.text = model.price0
                holder.price0.paintFlags = holder.price0.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Glide.with(holder.img.context).load(model.img).into(holder.img)
                if (model.isAdded) {
                    holder.addBtn.text = "ADDED"
                    holder.addBtn.setTextColor(resources.getColor(R.color.red))
                }else{
                    holder.addBtn.text = "ADD"
                    holder.addBtn.setTextColor(resources.getColor(android.R.color.white))
                }
                holder.addBtn.setOnClickListener {
                    model.isAdded = !model.isAdded
                    updateCart(model)
                    this.notifyItemChanged(position)
                }
                progressLayout.visibility = View.GONE
            }
        }
        adapter.notifyDataSetChanged()
        recview.setHasFixedSize(true)
        recview.layoutManager = LinearLayoutManager(activity as Context)
        recview.adapter = adapter
    }

    private class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: RelativeLayout = itemView.findViewById(R.id.container)
        val img: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.nametext)
        val save: TextView = itemView.findViewById(R.id.savetext)
        val price: TextView = itemView.findViewById(R.id.pricetext)
        val price0: TextView = itemView.findViewById(R.id.actualprice)
        val addBtn: Button = itemView.findViewById(R.id.add)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun processSearch(s: String) {
        var s1 = s
        if (s.isNotEmpty())
            s1 = s.toLowerCase(Locale.ROOT)
        val grocery = arrayListOf<GroceryModel>()
        grocery.clear()
        for (i in 0 until groceryArrayList.size) {
            if (groceryArrayList[i].name.toLowerCase(Locale.ROOT).contains(s1)) {
                val groceryObject = GroceryModel(
                    groceryArrayList[i].name,
                    groceryArrayList[i].save,
                    groceryArrayList[i].price,
                    groceryArrayList[i].img,
                    groceryArrayList[i].price0
                )
                grocery.add(groceryObject)
            }
            recyclerAdapter = HomeRecyclerAdapter(context as Context, grocery)
            recview.layoutManager = LinearLayoutManager(activity as Context)
            recview.adapter = recyclerAdapter
            recyclerAdapter.notifyDataSetChanged()
        }
    }

    private fun search() {
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                if (s.trim { it <= ' ' }.isNotEmpty()) {
                    adapter.stopListening()
                    searchFlag = 0
                    processSearch(s)
                }
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (s.trim { it <= ' ' }.isNotEmpty()) {
                    adapter.stopListening()
                    searchFlag = 0
                    processSearch(s)
                }
                return false
            }
        })
        search.setOnCloseListener {
            searchFlag = 1
//            search.isIconified = true
//            search.onActionViewCollapsed()
//            search.clearFocus()
            val query: Query = FirebaseFirestore.getInstance()
                    .collection("grocery")
            getList(query)
            adapter.startListening()
            true
        }
    }

    override fun onBackPressed(): Boolean {
        //return if (!search.isIconified) {
        return if (searchFlag == 0) {
            searchFlag = 1
            search.isIconified = true
            //search.onActionViewCollapsed()
            search.clearFocus()
            val query: Query = FirebaseFirestore.getInstance()
                    .collection("grocery")
            getList(query)
            adapter.startListening()
            true
        } else {
            false
        }
    }

}