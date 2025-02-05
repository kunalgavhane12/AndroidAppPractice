package com.example.alertdialogbox;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        //One Button Dialog
//        AlertDialog alertDialog = new AlertDialog.Builder(this)
//                .setTitle("Terms & Conditions")
//                .setIcon(R.drawable.outline_chat_info_24)
//                .setMessage("Have you read all terms and conditions?")
//                .setPositiveButton("Yes, I've Read", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "Yes, You can proceed now..", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .create();
//
//        alertDialog.show();


        //Two Button Dialog
        AlertDialog.Builder delDialog = new AlertDialog.Builder(MainActivity.this);
        delDialog.setTitle("Delete");
        delDialog.setIcon(R.drawable.baseline_delete_24);
        delDialog.setMessage("Do you want to Delete?");
        delDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //For Yes write Logic
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        delDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //For No
                Toast.makeText(MainActivity.this, "Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        delDialog.show();

//
//        @Override
//        public void onBackPressed() {
//            AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
//            exitDialog.setTitle("Exit?");
//            exitDialog.setMessage("Do you want to exit?");
//            exitDialog.setIcon(R.drawable.baseline_delete_24);
//
//            // No button to dismiss the dialog
//            exitDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(MainActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            // Yes button to perform action
//            exitDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    MainActivity.super.onBackPressed(); // Calls the super method to actually exit the activity
//                }
//            });
//
//            // Cancel button
//            exitDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(MainActivity.this, "Operation Cancelled!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            // Show the dialog
//            exitDialog.show();
//        }

    }
}