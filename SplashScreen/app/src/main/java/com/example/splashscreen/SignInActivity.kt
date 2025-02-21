package com.example.splashscreen

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var signupText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)

        // Initialize UI elements
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        signupText = findViewById(R.id.signupText)

        // Handle SignUp text underline on touch
        signupText.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> signupText.paintFlags = signupText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> signupText.paintFlags = signupText.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
            false
        }

        // Navigate to SignUp screen
        signupText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Set login button click listener
        loginButton.setOnClickListener {
            val userInput = username.text.toString().trim()
            val passInput = password.text.toString().trim()

            if (userInput.isEmpty() || passInput.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userInput == "user" && passInput == "1234") {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java)) // Navigate to Dashboard
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
