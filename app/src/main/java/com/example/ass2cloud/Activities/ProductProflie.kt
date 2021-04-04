package com.example.ass2cloud.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ass2cloud.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_proflie.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ProductProflie : AppCompatActivity() {
    var reTime : Long = 0
    var pTime : Long= 0
    var nameProd = ""
    val c = Calendar.getInstance()
    var cuDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)
    val sd = SimpleDateFormat("HH:mm:ss")
    var fb = FirebaseFirestore.getInstance()
    lateinit var mFirebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_proflie)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

         var cId = intent.getStringExtra("catId")!!
        getDataProduct()
        button2.setOnClickListener {
            var i = Intent(this, Products::class.java)
            i.putExtra("catId",cId)
            startActivity(i)

            finish()
        }



    }

    private fun trackScreen(screenName: String) {
        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Details")

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)



    }


    fun getDataProduct() {
        var  pId = intent.getStringExtra("productId")!!

        var ref = fb.collection("products").document(pId)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var data = documentSnapshot.data
                    var idd=documentSnapshot.id
                      nameProd = data!!["productName"] as String
                    var img = data["productImage"] as String
                    val type = data["productType"] as String

                    productName.text = nameProd
                    product_type_detail.text = type
                    supportActionBar!!.title = "$nameProd Details"
                    trackScreen("$nameProd Details")
                    selectContent(idd, nameProd)
                    // imageView3.setImageURI(Uri.parse(img))
                    if (img.isNotEmpty()) {
                        Picasso.get().load(img).into(product_img_details)
                    }else{

                    }

                    Log.e("dna", nameProd)
                } else {
                    Toast.makeText(this, "Failer", Toast.LENGTH_LONG).show()
                }

            }


            .addOnFailureListener { exception ->
                Toast.makeText(this, "$exception", Toast.LENGTH_LONG).show()
            }
    }
    fun selectContent(id: String, name: String){
        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        //  bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
    override fun onPause() {
        super.onPause()
        pTime = System.currentTimeMillis()
        val  datee = Date()


        val bundle= Bundle()
        bundle.putString("timeTracker", "Main " + " page" + java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime - reTime, TimeUnit.MILLISECONDS)) + " seconds")
        var finalTimee =  java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime - reTime, TimeUnit.MILLISECONDS)).toString() + " seconds"
        val map: MutableMap<String, Any> = HashMap()
        Toast.makeText(this,finalTimee, Toast.LENGTH_LONG).show()
        map["avgTime"] = finalTimee
        map["uId"] = "123"
        map["pageName"] = nameProd
        map["date"] =  cuDate
        map["timeOpen"] = sd.format(datee)


        fb.collection("data")
                .add(map)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        mFirebaseAnalytics.setUserProperty("timeTracker", "Main" + " page" + java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime - reTime, TimeUnit.MILLISECONDS)) + " seconds")

        mFirebaseAnalytics.logEvent("timeTracker", bundle)

    }

    override fun onResume() {

        super.onResume()
        reTime = System.currentTimeMillis()
    }

}