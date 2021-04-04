package com.example.ass2cloud.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ass2cloud.Activities.ProductProflie
import com.example.ass2cloud.Activities.Products
import com.example.ass2cloud.Models.PrdouctsModel
import com.example.ass2cloud.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.products_rc.view.*
import java.util.HashMap

class ProductsAdapter ( var data: MutableList<PrdouctsModel>,var con: Context, var cId:String) :
    RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {
    var mFirebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(con)
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_category=itemView.img_product_home
        var tv_cat_ad=itemView.name_product_home
        var card_rc_prod_home=itemView.card_rc_prod_home

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.products_rc, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        con =holder.card_rc_prod_home.context

        if (data[position].product_img_model!!.isNotEmpty()) {
            Picasso.get().load(data[position].product_img_model).into(holder.img_category)

        } else{

        }

        holder.tv_cat_ad.text = data[position].product_text_model
        selectContent(data[position].id!!,data[position].product_text_model!!)


        holder.card_rc_prod_home.setOnClickListener {

            var intent = Intent (con , ProductProflie::class.java)
            intent.putExtra("productId",data[position].id)
            intent.putExtra("catId",cId)
            Log.d("dna" ,data[position].id!!)
            con.startActivity(intent)
        }

    }


fun selectContent(id: String, name: String){
    var bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
    //  bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
}
}
