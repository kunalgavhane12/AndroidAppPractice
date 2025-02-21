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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var signInText: TextView
    private lateinit var signUpButton: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.editName)
        email = findViewById(R.id.editEmail)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        signInText = findViewById(R.id.signInText)
        signUpButton = findViewById(R.id.signupBtn)

        // Handle SignUp text underline on touch
        signInText.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> signInText.paintFlags = signInText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> signInText.paintFlags = signInText.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
            false
        }

        signInText.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        signUpButton.setOnClickListener {
            val Name = name.text.toString()
            val Email = email.text.toString()
            val Username = username.text.toString()
            val Password = password.text.toString()

            val user = User(Name, Email, Username, Password)
            database = FirebaseDatabase.getInstance().getReference("Users")

            // Add user data to Firebase
            database.child(Username).setValue(user)
                .addOnSuccessListener {
                    // Clear fields and show success message
                    name.text?.clear()
                    email.text?.clear()
                    username.text?.clear()
                    password.text?.clear()
                    Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Handle failure and show error message
                    Toast.makeText(this, "Failed to register: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}