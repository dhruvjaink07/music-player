package com.example.musicapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    lateinit var myRecyclerView: RecyclerView
    lateinit var myAdaptar: MyAdaptar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        myRecyclerView = findViewById(R.id.recyclerView)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofit.getData("eminem")

        retrofitData.enqueue(object : Callback<MusicData?> {
            override fun onResponse(p0: Call<MusicData?>, p1: Response<MusicData?>) {
                val dataList = p1.body()?.data!!

                myAdaptar = MyAdaptar(this@MainActivity,dataList)
                myRecyclerView.adapter = myAdaptar
                myRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                Log.d("onResponse", "onResponse: " + dataList?.get(0)?.title)
            }

            override fun onFailure(p0: Call<MusicData?>, p1: Throwable) {
                Log.d("onFailure", "onFailure: " + p1.message)
            }
        })

    }
}