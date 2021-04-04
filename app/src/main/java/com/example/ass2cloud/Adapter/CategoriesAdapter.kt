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
import com.example.ass2cloud.Activities.MainActivity
import com.example.ass2cloud.Activities.Products
import com.example.ass2cloud.Models.CategoriesModel
import com.example.ass2cloud.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.categories_rc.view.*
import java.util.HashMap

class CategoriesAdapter (var data: MutableList<CategoriesModel>, con: Context) :
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {

    lateinit var conte:Context

  var mFirebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(con)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img_category=itemView.img_category
        var tv_cat_ad=itemView.tv_category
        var card_cat_prof=itemView.card_cat




    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.categories_rc, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (data[position].category_img_model!!.isNotEmpty()){
            Picasso.get().load(data[position].category_img_model).into(holder.img_category)
        }else{

        }

        holder.tv_cat_ad.text = data[position].category_text_model
        conte=holder.card_cat_prof.context
        selectContent(data[position].id,data[position].category_text_model!!)
        holder.card_cat_prof.setOnClickListener {

            var intent = Intent (conte , Products::class.java)
            intent.putExtra("catId",data[position].id)
            conte.startActivity(intent)
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
