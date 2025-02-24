package com.example.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val data = findViewById<TextView>(R.id.Data)
        val name = intent.getStringExtra(SignInActivity.KEY)
        val email = intent.getStringExtra(SignInActivity.KEY1)
        val username = intent.getStringExtra(SignInActivity.KEY2)

//        data.setText("Name: "+ name + "\nEmail: "+email +"\nUser: "+ username)
        data.text = "Name: $name\nEmail: $email\nUser: $username"
    }
}