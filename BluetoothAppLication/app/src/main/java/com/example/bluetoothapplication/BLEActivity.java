package com.example.bluetoothapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Set;

public class BLEActivity extends AppCompatActivity {

    static BluetoothManager btManager;
    static BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    Button stopScanningButton;
    Button disconnectDevice;
    Boolean btScanning = false;
    ArrayList<BluetoothDevice> devicesDiscovered = new ArrayList<>();
    ArrayList<String> ListElementsArrayList = new ArrayList<>();
    static BluetoothGatt bluetoothGatt;
    ListView deviceListView;
    TextView DeviceText;
    TextView ConnectDevice;
    BluetoothDevice connectedDevice = null;
    private static final long SCAN_PERIOD = 10000;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 2;
    private static final int PERMISSION_REQUEST_BLUETOOTH_SCAN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        DeviceText = findViewById(R.id.txtDevice);
        startScanningButton = findViewById(R.id.StartScanButton);
        stopScanningButton = findViewById(R.id.StopScanButton);
        ConnectDevice = findViewById(R.id.ConnectDevice);
        deviceListView = findViewById(R.id.listView);
        stopScanningButton.setVisibility(View.INVISIBLE);

        // Initialize the device list and adapter
        ArrayAdapter<String> deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListElementsArrayList);
        deviceListView.setAdapter(deviceListAdapter);

        // Initialize Bluetooth
        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        // Check if Bluetooth is supported
        if (btAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display the local device name
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
            return;
        }

        DeviceText.setText("Local Device: " +  btAdapter.getName());

        // Check if a device is already connected and update the TextView
        if (bluetoothGatt != null && connectedDevice != null) {
            ConnectDevice.setText("Connected Device: " + connectedDevice.getName());
        } else {
            ConnectDevice.setText("Connected Device: None");
        }

        // Request necessary permissions
        requestPermissions();

        // Set button click listeners
        startScanningButton.setOnClickListener(v -> startScanning());
        stopScanningButton.setOnClickListener(v -> stopScanning());

        // Set ListView item click listener
        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = devicesDiscovered.get(position);
            connectToDevice(selectedDevice);
        });

        // Set disconnect button click listener
//        disconnectDevice.setOnClickListener(v -> {
//            if (bluetoothGatt != null) {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
//                    return;
//                }
//                bluetoothGatt.disconnect();
//                bluetoothGatt.close();
//                bluetoothGatt = null;
//                Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure Bluetooth is enabled every time the activity resumes
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            // Bluetooth is already enabled, initialize the scanner
            btScanner = btAdapter.getBluetoothLeScanner();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth was enabled, initialize the scanner
                btScanner = btAdapter.getBluetoothLeScanner();
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            } else {
                // User denied the request to enable Bluetooth
                Toast.makeText(this, "Bluetooth is required to use this app", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void requestPermissions() {
        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }

        // Request Bluetooth scan permission (for Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
            }
        }
    }

    private void startScanning() {
        if (!btScanning) {
            // Check permissions before starting the scan
            if (checkPermissions()) {
                btScanning = true;
                devicesDiscovered.clear(); // Clear the list of discovered devices
                ListElementsArrayList.clear(); // Clear the list of device names/addresses
                ((ArrayAdapter<?>) deviceListView.getAdapter()).notifyDataSetChanged(); // Notify the adapter
                startScanningButton.setVisibility(View.INVISIBLE);
                stopScanningButton.setVisibility(View.VISIBLE);

                // Configure scan settings for low latency
                ScanSettings scanSettings = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    scanSettings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                            .build();
                }

                // Start BLE scan
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
                    return;
                }
                btScanner.startScan(null, scanSettings, leScanCallback); // Pass null for no filters
                Toast.makeText(this, "Scanning started", Toast.LENGTH_SHORT).show();

                // Start Classic Bluetooth scan
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    startClassicBluetoothScan();
                }

                // Stop scanning after 5 seconds
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (btScanning) {
                        stopScanning();
                    }
                }, SCAN_PERIOD); // 10000 milliseconds = 10 seconds
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopScanning() {
        if (btScanning) {
            btScanning = false;
            startScanningButton.setVisibility(View.VISIBLE);
            stopScanningButton.setVisibility(View.INVISIBLE);

            // Stop BLE scan
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
                return;
            }
            btScanner.stopScan(leScanCallback);

            // Stop Classic Bluetooth scan
            if (btAdapter != null) {
                btAdapter.cancelDiscovery();
            }

            Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermissions() {
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        // Check if Bluetooth scan permission is granted (for Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    // ScanCallback to handle BLE scan results
    private final ScanCallback leScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            if (ActivityCompat.checkSelfPermission(BLEActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BLEActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
                return;
            }
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            Log.d("BLE", "Discovered Device: " + deviceName + " - " + deviceAddress);

            // Check if the device is already in the list
            if (deviceName != null && !isDeviceAlreadyAdded(deviceAddress)) {
                devicesDiscovered.add(device);
                ListElementsArrayList.add(deviceName + "\n" + deviceAddress);

                // Update the ListView on the main thread
                runOnUiThread(() -> {
                    ((ArrayAdapter<?>) deviceListView.getAdapter()).notifyDataSetChanged();
                    Log.i("BLE", "Device added to list: " + deviceName + " - " + deviceAddress);
                });
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("BLE", "Scan failed with error code: " + errorCode);
            runOnUiThread(() -> Toast.makeText(BLEActivity.this, "Scan failed: " + errorCode, Toast.LENGTH_SHORT).show());
        }
    };

    // Helper method to check if a device is already in the list
    private boolean isDeviceAlreadyAdded(String deviceAddress) {
        for (BluetoothDevice device : devicesDiscovered) {
            if (device.getAddress().equals(deviceAddress)) {
                return true; // Device already exists in the list
            }
        }
        return false; // Device is not in the list
    }

    // Classic Bluetooth scanning
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void startClassicBluetoothScan() {
        if (btAdapter != null && btAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
                return;
            }

            // Start discovering Bluetooth Classic devices
            btAdapter.startDiscovery();

            // Register a BroadcastReceiver to listen for discovered devices
            registerReceiver(bluetoothDeviceReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    private final BroadcastReceiver bluetoothDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();

                    if (deviceName != null && !isDeviceAlreadyAdded(deviceAddress)) {
                        devicesDiscovered.add(device);
                        ListElementsArrayList.add(deviceName + "\n" + deviceAddress);

                        // Update the ListView on the main thread
                        runOnUiThread(() -> {
                            ((ArrayAdapter<?>) deviceListView.getAdapter()).notifyDataSetChanged();
                            Log.i("BLE", "Classic Device added to list: " + deviceName + " - " + deviceAddress);
                        });
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btScanning) {
            stopScanning();
        }
        // Unregister the BroadcastReceiver
        unregisterReceiver(bluetoothDeviceReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION || requestCode == PERMISSION_REQUEST_BLUETOOTH_SCAN) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to connect to a Bluetooth device
    private void connectToDevice(BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
            return;
        }

        // Stop scanning before connecting
        if (btScanning) {
            stopScanning();
        }

        // Connect to the device
        if (bluetoothGatt != null) {
            bluetoothGatt.close(); // Close any existing connection
        }
        bluetoothGatt = device.connectGatt(this, false, gattCallback);
        Toast.makeText(this, "Connecting to " + device.getName(), Toast.LENGTH_SHORT).show();
    }

    // BluetoothGattCallback to handle connection events
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                // Device connected
                runOnUiThread(() -> {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    connectedDevice = gatt.getDevice();
                    ConnectDevice.setText("Connected Device: " + connectedDevice.getName());
                    Toast.makeText(BLEActivity.this, "Connected to " + gatt.getDevice().getName(), Toast.LENGTH_SHORT).show();
                    Log.i("BLE", "Connected to " + gatt.getDevice().getName());
                });

                // Discover services
                if (ActivityCompat.checkSelfPermission(BLEActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BLEActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_REQUEST_BLUETOOTH_SCAN);
                    return;
                }
                gatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                // Device disconnected
                runOnUiThread(() -> {
                    ConnectDevice.setText("Connected Device: None");
                    Toast.makeText(BLEActivity.this, "Disconnected from " + gatt.getDevice().getName(), Toast.LENGTH_SHORT).show();
                    Log.i("BLE", "Disconnected from " + gatt.getDevice().getName());
                    connectedDevice = null; // Reset the connected device
                });
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Services discovered
                runOnUiThread(() -> {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Toast.makeText(BLEActivity.this, "Services discovered for " + gatt.getDevice().getName(), Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Log.i("BLE", "Services discovered for " + gatt.getDevice().getName());
                });
            } else {
                // Failed to discover services
                runOnUiThread(() -> {
                    Toast.makeText(BLEActivity.this, "Failed to discover services for " + gatt.getDevice().getName(), Toast.LENGTH_SHORT).show();
                    Log.e("BLE", "Failed to discover services for " + gatt.getDevice().getName());
                });
            }
        }
    };
}