package com.example.tablayoutexample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);

        // Create and set the adapter for ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Link TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Chats"); break;
                case 1: tab.setText("Status"); break;
                case 2: tab.setText("Calls"); break;
            }
        }).attach();
    }
}
