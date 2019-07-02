package com.example.mviexample.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mviexample.R
import com.example.mviexample.view.search.SearchActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this@MainActivity, SearchActivity::class.java)
        startActivity(intent)

    }
}
