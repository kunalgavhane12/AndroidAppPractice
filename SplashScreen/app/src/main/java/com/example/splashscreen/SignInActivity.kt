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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var signupText: TextView
    private lateinit var  databaseReference: DatabaseReference;

    companion object{
        const val KEY = "com.example.splashscreen.SignInActivity.name"
        const val KEY1 = "com.example.splashscreen.SignInActivity.mail"
        const val KEY2 = "com.example.splashscreen.SignInActivity.user"
    }

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
            finish()
        }

        // Set login button click listener
        loginButton.setOnClickListener {
            val userInput = username.text.toString().trim()
            val passInput = password.text.toString().trim()

            if (userInput.isEmpty() || passInput.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            readData(userInput);
//            if (userInput == "user" && passInput == "1234") {
//                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, DashboardActivity::class.java)) // Navigate to Dashboard
//                finish()
//            } else {
//                Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private fun readData(username: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(username).get().addOnSuccessListener {
            if(it.exists()) {
                Toast.makeText(this, "Found", Toast.LENGTH_SHORT).show()
                val email = it.child("email").value
                val name = it.child("name").value
                val password = it.child("password").value
                val user = it.child("username").value
                Toast.makeText(this, "Email: "+ email + "Name: " + name
                    +"User: "+ user+ "Password: "+ password, Toast.LENGTH_SHORT).show()
                val intentWelcome = Intent(this, DashboardActivity::class.java)
                intentWelcome.putExtra(KEY, name.toString())
                intentWelcome.putExtra(KEY1, email.toString())
                intentWelcome.putExtra(KEY2, user.toString())
                startActivity(intentWelcome)
            } else {
                Toast.makeText(this, "User Doest not exist", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener {
            Toast.makeText(this,"User not Found Please register.", Toast.LENGTH_SHORT).show()
        }

    }
}
