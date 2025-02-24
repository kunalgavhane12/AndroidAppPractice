package com.example.viewbinding

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.viewbinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //binding initialize view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtHello.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> binding.txtHello.paintFlags = binding.txtHello.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL -> binding.txtHello.paintFlags = binding.txtHello.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
            }
            false
        }

        binding.button.setOnClickListener {
            if (binding.checkBox.isChecked){
                Toast.makeText(applicationContext,"Text Clicked" , Toast.LENGTH_SHORT).show()
                binding.checkBox.buttonTintList  =ColorStateList.valueOf(Color.BLACK)
            } else {
                binding.checkBox.buttonTintList = ColorStateList.valueOf(Color.RED)
                Toast.makeText(this, "Text Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}