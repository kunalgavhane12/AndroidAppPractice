package com.example.usbinterfaceapplication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

        // Register BroadcastReceiver for USB events
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(USB_PERMISSION);
        registerReceiver(usbReceiver, filter);

        // Initial device check
        checkForConnectedDevices();

        // Refresh button click event
        refreshButton.setOnClickListener(v -> checkForConnectedDevices());
    }

    // Method to check connected USB devices
    private void checkForConnectedDevices() {
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        if (deviceList.isEmpty()) {
            updateUI("Disconnected", "Not Available");
        } else {
            StringBuilder deviceDetails = new StringBuilder();
            for (UsbDevice device : deviceList.values()) {
                deviceDetails.append(device.getDeviceName()).append("\n"); // Show device name
                requestPermission(device);
            }
            updateUI("Connected", deviceDetails.toString());
        }
    }

    // Request permission to access USB device
    private void requestPermission(UsbDevice device) {
        if (!usbManager.hasPermission(device)) {
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
            usbManager.requestPermission(device, permissionIntent);
        }
    }

    // Update UI dynamically
    private void updateUI(String status, String deviceName) {
        runOnUiThread(() -> {
            statusTextView.setText("Status: " + status);
            deviceNameTextView.setText("Device Name: " + deviceName);
        });
    }

    // BroadcastReceiver to detect USB events
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                checkForConnectedDevices();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                updateUI("Disconnected", "Not Available");
            } else if (USB_PERMISSION.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) && device != null) {
                    checkForConnectedDevices();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbReceiver);
    }
}
