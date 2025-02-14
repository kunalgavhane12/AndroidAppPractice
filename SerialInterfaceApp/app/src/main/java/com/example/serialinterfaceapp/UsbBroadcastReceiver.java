package com.example.serialinterfaceapp;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.TextView;
import android.app.Activity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UsbBroadcastReceiver extends BroadcastReceiver {
    private TextView statusTextView;
    private TextView dataTextView;
    public static UsbDeviceConnection connection;
    public static UsbSerialDriver driver;
    public static UsbSerialPort port;
    private static final String ACTION_USB_PERMISSION = "com.example.serialinterfaceapp.USB_PERMISSION";

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
                updateDataTextView(context, "USB Permission Asking");
                requestUsbPermission(context, usbManager, device);
            }
            updateTextView(context, "Device: " + device.getProductName() + "\nPID: " +
                    device.getProductId() + "\nVID: " + device.getVendorId() + "\nDevice Id: "
                    + device.getDeviceId());
        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
            updateTextView(context, "Device: None");
            updateDataTextView(context, "USB Device Disconnected");
        } else if (ACTION_USB_PERMISSION.equals(action)) {
            synchronized (this) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) && device != null) {
                    try {
                        handleUsbDevice(context, usbManager, device);
                    } catch (IOException e) {
                        updateDataTextView(context, "Error handling USB: " + e.getMessage());
                    }
                } else {
                    updateDataTextView(context, "USB Permission Denied");
                }
            }
        }
    }

    private void requestUsbPermission(Context context, UsbManager usbManager, UsbDevice device) {
        if (!usbManager.hasPermission(device)) {
            updateDataTextView(context, "USB Permission request");
            showPermissionDialog(context, usbManager, device);
        } else {
            try {
                handleUsbDevice(context, usbManager, device);
            } catch (IOException e) {
                updateDataTextView(context, "Error: " + e.getMessage());
            }
        }
    }

    private void showPermissionDialog(Context context, UsbManager usbManager, UsbDevice device) {
        Activity activity = (Activity) context;
        activity.runOnUiThread(() -> {
            new AlertDialog.Builder(activity)
                    .setTitle("USB Permission Required")
                    .setMessage("This app needs permission to access the USB device. Grant permission?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0,
                                new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        usbManager.requestPermission(device, permissionIntent);
                    })
                    .setNegativeButton("No", (dialog, which) -> updateDataTextView(context, "USB Permission Denied"))
                    .setCancelable(false)
                    .show();
        });
    }

    private void handleUsbDevice(Context context, UsbManager usbManager, UsbDevice device) throws IOException {

        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        driver = usbDefaultProber.probeDevice(device);

        if (driver == null) {
            updateDataTextView(context, "No USB Serial Driver found.");
            return;
        }

        connection = usbManager.openDevice(driver.getDevice());

        if (connection == null) {
            updateDataTextView(context, "Failed to open USB device connection.");
            return;
        }

        // Get the first port and open it
        port = driver.getPorts().get(0);
        if (port == null) {
            updateDataTextView(context, "No available USB serial port.");
            return;
        }

        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            updateDataTextView(context, "USB Device Connected and Port Opened!");
        } catch (IOException e) {
            updateDataTextView(context, "Error opening port: " + e.getMessage());
        }
    }

    private void updateTextView(Context context, String message) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> statusTextView.setText(message));
        }
    }

    private void updateDataTextView(Context context, String message) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> dataTextView.setText(message));
        }
    }
}
