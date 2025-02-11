package com.example.bottomnavigationapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
BottomNavigationView bnView;
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

        bnView = findViewById(R.id.bnView);
        loadFrag(new HomeFragment() , 0);
        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    loadFrag(new HomeFragment() , 0);
                }else if (id == R.id.nav_Search) {
                    loadFrag(new SearchFragment(), 1);
                }else if (id == R.id.nav_Utilities) {
                    loadFrag(new UtilitiesFragment(), 1);
                }else if (id == R.id.nav_ContactUs) {
                    loadFrag(new ContactsUsFragment(), 1);
                }else {
                    loadFrag(new ProfileFragment(), 1);
                }
                return true;
            }
        });

    }

    public void loadFrag(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(flag == 0) {
            fragmentTransaction.add(R.id.container, fragment);
        } else {
            fragmentTransaction.replace(R.id.container, fragment);
        }
        fragmentTransaction.commit();
    }
}