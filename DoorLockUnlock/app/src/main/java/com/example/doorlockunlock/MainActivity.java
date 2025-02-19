package com.example.doorlockunlock;

import android.app.AlertDialog;
import android.app.ComponentCaller;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    AppCompatButton btnLock, btnUnlock;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "device").commit();
        } else {
            onBackStackChanged();
        }

//        txtStatus = findViewById(R.id.txtStatus);
//        btnLock = findViewById(R.id.btnLock);
//        btnUnlock = findViewById(R.id.btnUnlock);
//
//        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
//

//        btnLock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              /*  new AlertDialog.Builder(MainActivity.this)
//                        .setTitle("Confirm")
//                        .setMessage("Are you sure you want to lock the door?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MainActivity.this, "Door Locked", Toast.LENGTH_SHORT).show();
//                                txtStatus.setText("Door Locked");
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, null)
//                        .show();*/
//                Toast.makeText(MainActivity.this, "Door Locked", Toast.LENGTH_SHORT).show();
//                txtStatus.setText("Door Locked");
//            }
//        });
//
//        btnUnlock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Door Unlocked", Toast.LENGTH_SHORT).show();
//                txtStatus.setText("Door Unlocked");
//            }
//        });
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            txtStatus.setText("USB Device detected");
        }
        super.onNewIntent(intent);
    }
}
