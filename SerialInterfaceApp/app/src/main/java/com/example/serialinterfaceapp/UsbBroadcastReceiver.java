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
    public static UsbDeviceConnection connection;
    public static List<UsbSerialDriver> availableDrivers;
    public static UsbSerialDriver driver;
    public static UsbSerialPort port;
    public static Map<String, UsbDevice> devices;
    public static UsbDevice device = null;
    private static String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    public UsbBroadcastReceiver(TextView statusTextView) {
        this.statusTextView = statusTextView;
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

                // Check device type explicitly and handle accordingly
                if (isMassStorageDevice(device)) {
                    handleMassStorageDevice(context, device);
                } else if (isSerialDevice(device)) {
                    handleSerialDevice(context, device);
                } else {
                    handleOtherUsbDevice(context, device);
                }
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
                        if (isMassStorageDevice(device)) {
                            handleMassStorageDevice(context, device);
                        } else if (isSerialDevice(device)) {
                            handleSerialDevice(context, device);
                        }
                    }
                } else {
                    updateTextView((Activity) context, "USB Permission Denied");
                }
            }
        }
    }

    // Check if the device is a mass storage device (e.g., USB flash drive)
    private boolean isMassStorageDevice(UsbDevice device) {
        for (int i = 0; i < device.getInterfaceCount(); i++) {
            if (device.getInterface(i).getInterfaceClass() == 0x08) {
                updateTextView(null, "Mass Storage Device Detected");
                return true;
            }
        }
        return false;
    }

    // Check if the device is a serial device (e.g., CDC-ACM for ESP32)
    private boolean isSerialDevice(UsbDevice device) {
        boolean isSerial = false;

        for (int i = 0; i < device.getInterfaceCount(); i++) {
            int interfaceClass = device.getInterface(i).getInterfaceClass();
            int interfaceSubclass = device.getInterface(i).getInterfaceSubclass();
            int interfaceProtocol = device.getInterface(i).getInterfaceProtocol();

            // Debug log for device interface details
            updateTextView(null, "Interface Class: " + interfaceClass +
                    " Subclass: " + interfaceSubclass +
                    " Protocol: " + interfaceProtocol);

            if (interfaceClass == 0x02) {  // CDC-ACM Class Code
                isSerial = true;
            }
        }

        if (isSerial) {
            updateTextView(null, "Serial Device (CDC-ACM) Detected");
        }
        return isSerial;
    }

    // Handle mass storage devices (e.g., USB drives)
    private void handleMassStorageDevice(Context context, UsbDevice device) {
        updateTextView((Activity) context, "Mass Storage Device Connected: " + device.getProductName());
        // Your logic to handle mass storage device
    }

    // Handle serial devices (e.g., ESP32, USB to Serial adapters)
    private void handleSerialDevice(Context context, UsbDevice device) {
        updateTextView((Activity) context, "Serial Device Connected: " + device.getProductName());
        connectToUsbSerial(context, (UsbManager) context.getSystemService(Context.USB_SERVICE), device);
    }

    // Handle other USB devices (e.g., laptops or desktops)
    private void handleOtherUsbDevice(Context context, UsbDevice device) {
        String productName = device.getProductName();
        String vendorName = device.getManufacturerName();
        String deviceInfo = "Unknown USB Device Connected: " + productName;

        // Check for specific known vendor IDs and product IDs
        int vendorId = device.getVendorId();
        int productId = device.getProductId();

        // Example known vendor and product IDs for certain USB devices
        if (vendorId == 0x1B1C && productId == 0x0C06) {
            deviceInfo = "Laptop/Desktop Detected (Vendor: " + vendorName + ")";
        }

        updateTextView((Activity) context, deviceInfo);
    }

    // Connect to a USB serial device (if it's a serial device)
    private void connectToUsbSerial(Context context, UsbManager usbManager, UsbDevice device) {
        // Explicitly use the USB serial prober to check for CDC-ACM
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);

        if (availableDrivers.isEmpty()) {
            updateTextView((Activity) context, "No Serial Device Found");
            return;
        }

        driver = availableDrivers.get(0);
        connection = usbManager.openDevice(driver.getDevice());

        if (connection == null) {
            updateTextView((Activity) context, "Failed to Open Connection");
            return;
        }

        port = driver.getPorts().get(0);
        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            updateTextView((Activity) context, "Serial Device Connected: " + device.getProductName());
        } catch (Exception e) {
            updateTextView((Activity) context, "Error: " + e.getMessage());
        }
    }

    // Update the TextView with a message on the UI thread
    private void updateTextView(Activity activity, String message) {
        if (activity != null) {
            activity.runOnUiThread(() -> statusTextView.setText(message));
        } else {
            // Log output or show a default message if the activity is null
            statusTextView.setText(message);
        }
    }
}
