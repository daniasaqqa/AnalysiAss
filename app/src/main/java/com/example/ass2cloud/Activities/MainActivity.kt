package com.example.ass2cloud.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ass2cloud.Adapter.CategoriesAdapter
import com.example.ass2cloud.Models.CategoriesModel
import com.example.ass2cloud.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
var resumeTime : Long = 0
   var pTime : Long= 0

  val sd = SimpleDateFormat("HH:mm:ss")
   val c = Calendar.getInstance()
    var cuDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)

    var fb = FirebaseFirestore.getInstance()
    lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        supportActionBar!!.title = "Category"

        trackScreen("Categories")
        getData()

    }




    private fun trackScreen(screenName: String) {

        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Category")

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)



    }

   private fun getData(){
            val catProfData= mutableListOf<CategoriesModel>()
            fb.collection("category")
                    .get()
                    .addOnCompleteListener { querySnapshot->
                        if(querySnapshot.isSuccessful){
                            for (document in querySnapshot.result!!){
                                val id:String =document.id

                                val data=document.data
                                val productName = data["CatName"] as String?
                                val productImage = data["CatImg"] as String?

                                catProfData.add(
                                        CategoriesModel(
                                                id,
                                                productImage,
                                                productName

                                        )
                                )
                            }
                            rc_categories_profile.layoutManager = GridLayoutManager(this@MainActivity, 1)
                            rc_categories_profile.setHasFixedSize(true)
                            val Adapter = CategoriesAdapter(catProfData, this@MainActivity)
                            rc_categories_profile.adapter = Adapter
                        }
                    }

        }
    override fun onPause() {
        super.onPause()
        pTime  = System.currentTimeMillis()
        val  datee = Date()

        val bundle = Bundle()
        bundle.putString("timeTracker", "Main " + " page" + java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime  - resumeTime, TimeUnit.MILLISECONDS)) + " seconds")
      var finalTimee =  java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime  - resumeTime, TimeUnit.MILLISECONDS)).toString() + " seconds"
        Log.d("aaaa", finalTimee)
        val map: MutableMap<String, Any> = HashMap()

        Toast.makeText(this,finalTimee, Toast.LENGTH_LONG).show()
        map["avgTime"] = finalTimee
        map["uId"] = "123"
        map["pageName"] = "MainActivity"
        map["date"] =  cuDate
        map["timeOpen"] = sd.format(datee)


        fb.collection("data")

                .add(map)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        mFirebaseAnalytics.setUserProperty("timeTracker", "Main" + " page" + java.lang.String.valueOf(TimeUnit.SECONDS.convert(pTime  - resumeTime, TimeUnit.MILLISECONDS)) + " seconds")
        mFirebaseAnalytics.logEvent("timeTracker", bundle)
    }
    override fun onResume() {
        super.onResume()
        resumeTime = System.currentTimeMillis()
    }
}
