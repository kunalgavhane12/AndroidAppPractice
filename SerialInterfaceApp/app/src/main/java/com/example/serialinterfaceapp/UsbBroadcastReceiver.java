package com.example.serialinterfaceapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.TextView;
import android.app.Activity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.List;
import java.util.Map;

public class UsbBroadcastReceiver extends BroadcastReceiver {
    private TextView statusTextView;
    private TextView dataTextView;
    public static UsbDeviceConnection connection;
    public static List<UsbSerialDriver> availableDrivers;
    public static UsbSerialDriver driver;
    public static UsbSerialPort port;
    public static Map<String, UsbDevice> devices;
    public static UsbDevice device = null;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    public UsbBroadcastReceiver(TextView statusTextView, TextView dataTextView) {
        this.statusTextView = statusTextView;
        this.dataTextView = dataTextView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device != null) {
                PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0,
                        new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
                usbManager.requestPermission(device, permissionIntent);
                handleUsbDevice(context, usbManager, device);
            }
        }

        if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
            updateTextView((Activity) context, "USB Device Disconnected");
        }

        if (ACTION_USB_PERMISSION.equals(action)) {
            synchronized (this) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if (device != null) {
                        handleUsbDevice(context, usbManager, device);
                    }
                } else {
                    updateTextView((Activity) context, "USB Permission Denied");
                }
            }
        }
    }

    // Handle USB device (serial, mass storage, or other types)
    private void handleUsbDevice(Context context, UsbManager usbManager, UsbDevice device) {
        updateTextView((Activity) context, "Device: " + device.getProductName());
    }

    // Update the TextView with a message on the UI thread
    private void updateTextView(Activity activity, String message) {
        if (activity != null) {
            activity.runOnUiThread(() -> statusTextView.setText(message));
        } else {
            statusTextView.setText(message);
        }
    }
}