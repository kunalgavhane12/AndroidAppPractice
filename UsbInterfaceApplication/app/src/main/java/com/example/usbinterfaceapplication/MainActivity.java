package com.example.usbinterfaceapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView;
    private TextView deviceNameTextView;
    private UsbManager usbManager;
    private static final String USB_PERMISSION = "com.example.usbinterfaceapplication.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        statusTextView = findViewById(R.id.statusTextView);
        deviceNameTextView = findViewById(R.id.deviceNameTextView);
        Button refreshButton = findViewById(R.id.refreshButton);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // Check for connected USB devices at startup
        checkForConnectedDevices();

        // Refresh button click event
        refreshButton.setOnClickListener(v -> checkForConnectedDevices());
    }

    // Method to check if a USB device is connected
    private void checkForConnectedDevices() {
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        if (deviceList.isEmpty()) {
            statusTextView.setText("Status: Disconnected");
            deviceNameTextView.setText("Device Name: Not Available");
        } else {
            StringBuilder deviceDetails = new StringBuilder("Device Name:  ");
            for (UsbDevice device : deviceList.values()) {
                deviceDetails.append(device.getDeviceName()).append("\n"); // Show device name
                requestPermission(device); // Request permission to access the USB device
            }
            statusTextView.setText("Status: Connected");
            deviceNameTextView.setText(deviceDetails.toString());
        }
    }

    // Method to request permission to access the USB device
    private void requestPermission(UsbDevice device) {
        if (!usbManager.hasPermission(device)) {
            // Use FLAG_IMMUTABLE for permission request for Android 12 and above
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
            usbManager.requestPermission(device, permissionIntent);
        }
    }
}