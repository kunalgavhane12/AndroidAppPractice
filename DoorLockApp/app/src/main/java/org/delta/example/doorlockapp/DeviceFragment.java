package org.delta.example.doorlockapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DeviceFragment extends Fragment {

    private BluetoothAdapter btAdapter;
    private BluetoothLeScanner btScanner;
    private final ArrayList<String> deviceList = new ArrayList<>();
    private final Set<String> uniqueDevices = new HashSet<>(); // To avoid duplicates
    private ArrayAdapter<String> listAdapter;
    private ListView listView;
    private boolean isScanning = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_list_item, container, false);

        // Initialize Bluetooth Adapter
        BluetoothManager btManager = (BluetoothManager) requireActivity().getSystemService(getContext().BLUETOOTH_SERVICE);
        if (btManager != null) {
            btAdapter = btManager.getAdapter();
            if (btAdapter != null) {
                btScanner = btAdapter.getBluetoothLeScanner();
            }
        }

        // Initialize UI elements
        listView = view.findViewById(R.id.listView1);
        Button scanButton = view.findViewById(R.id.StartScanButton);
        scanButton.setOnClickListener(v -> startScan());

        // Initialize list adapter
        listAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, deviceList);
        listView.setAdapter(listAdapter);

        setHasOptionsMenu(true); // Enable menu options
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_devices, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            startScan();
            return true;
        } else if (item.getItemId() == R.id.Disconnet) {
            disconnect();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startScan() {
        if (btAdapter == null || !btAdapter.isEnabled()) {
            Toast.makeText(getContext(), "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isScanning) {
            Toast.makeText(getContext(), "Already scanning...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear previous scan results
        deviceList.clear();
        uniqueDevices.clear();
        listAdapter.notifyDataSetChanged();

        Toast.makeText(getContext(), "Scanning for Bluetooth devices...", Toast.LENGTH_SHORT).show();

        isScanning = true;
        btScanner.startScan(leScanCallback);
    }

    private void stopScan() {
        if (isScanning) {
            isScanning = false;
            btScanner.stopScan(leScanCallback);
            Toast.makeText(getContext(), "Scan stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private final ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if (device != null) {
                String deviceInfo = (device.getName() != null ? device.getName(): "Unknown") + " - " + device.getAddress();
                // Avoid duplicate entries
                if (uniqueDevices.add(deviceInfo)) {
                    deviceList.add(deviceInfo);
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void disconnect() {
        stopScan();
        Toast.makeText(getContext(), "Disconnected from devices", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScan();
    }
}
