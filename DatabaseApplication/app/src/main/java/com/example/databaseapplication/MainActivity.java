package com.example.databaseapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
//        myDBHelper.addContact("Kunal", "+919922257744");
//        myDBHelper.addContact("Kunal1", "+919922257741");
//        myDBHelper.addContact("Kunal2", "+919922257742");
//        myDBHelper.addContact("Kunal3", "+919922257743");
//        myDBHelper.addContact("Kunal4", "+919922257745");
//
//        ContactModel contactModel = new ContactModel();
//        contactModel.id = 1;
//        contactModel.name = "xyz";
//        contactModel.phone_no = "1234567890";
//
//        myDBHelper.updateContact(contactModel);

        myDBHelper.deleteContact(1);
        ArrayList<ContactModel> arrContacts = myDBHelper.fetchContact();

        for (int i=0; i < arrContacts.size(); i++) {
            Log.i("Contact_INFO", "Name: " + arrContacts.get(i).name + " Phone: " + arrContacts.get(i).phone_no);
        }
    }
}