package com.example.debtorslist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.debtorslist.R
import com.example.debtorslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb)
        supportActionBar?.title = getString(R.string.app_name)
    }
}