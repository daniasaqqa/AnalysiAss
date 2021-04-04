package com.example.ass2cloud.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ass2cloud.Adapter.ProductsAdapter
import com.example.ass2cloud.Models.PrdouctsModel
import com.example.ass2cloud.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_products.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Products : AppCompatActivity() {
    var reTime : Long = 0
    var pTime : Long= 0
    var type = ""
    val c = Calendar.getInstance()
    var cuDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)
    val sd = SimpleDateFormat("HH:mm:ss")
    var fb = FirebaseFirestore.getInstance()
    var nameCat = ""
    lateinit var mFirebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        headName()

        supportActionBar!!.title = "Products"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



    }

    private fun headName() {



       var  cid = intent.getStringExtra("catId")!!

        var ref = fb.collection("category").document(cid)

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {

                    var data = documentSnapshot.data
                     nameCat = data!!["CatName"] as String
                    supportActionBar!!.title = nameCat
                    productData(nameCat,cid)
                    trackScreen(nameCat)
                } else {
                    Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this, "$exception", Toast.LENGTH_LONG).show()
            }


    }

    private fun productData(nameC :String,cId : String) {

        val dataCat = mutableListOf<PrdouctsModel>()
        fb.collection("products").whereEqualTo("productType", nameC)
            .get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    for (document in querySnapshot.result!!) {
                        val id = document.id
                        val data = document.data
                        val productName = data["productName"] as String?
                        val productImage = data["productImage"] as String?
                        type = data["productType"] as String
                        dataCat.add(
                            PrdouctsModel(
                                id,
                                productImage,
                                productName

                            )
                        )
                    }

                    product_rec.layoutManager = GridLayoutManager(this@Products, 2)
                    product_rec.setHasFixedSize(true)
                    val Adapter = ProductsAdapter(dataCat,this@Products,cId)
                    product_rec.adapter = Adapter
                }

            }

    }

    private fun trackScreen(screenName: String) {
        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Products")

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)



    }

    override fun onPause() {
        super.onPause()
        pTime  = System.currentTimeMillis()
        val  datee = Date()

        val bundle = Bundle()
        bundle.putString("timeTracker", "Main " + " page" + java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime  - reTime, TimeUnit.MILLISECONDS)) + " seconds")
        var finalTimee =  java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime  - reTime, TimeUnit.MILLISECONDS)).toString() + " seconds"
        Log.d("aaaa", finalTimee)
        val map: MutableMap<String, Any> = HashMap()

        Toast.makeText(this,finalTimee, Toast.LENGTH_LONG).show()
        map["avgTime"] = finalTimee
        map["uId"] = "123"
        map["pageName"] = type
        map["date"] =  cuDate
        map["timeOpen"] = sd.format(datee)
        fb.collection("data")
                .add(map)
                .addOnSuccessListener {

                }
                .addOnFailureListener {
                }
        mFirebaseAnalytics.setUserProperty("timeTracker", "Main" + " page" + java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime  - reTime, TimeUnit.MILLISECONDS)) + " seconds")
        mFirebaseAnalytics.logEvent("timeTracker", bundle)
    }

    override fun onResume() {
        super.onResume()

        reTime = System.currentTimeMillis()
    }

}