package org.delta.example.doorlockapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class TerminalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageButton buttonSend, btnUnlock;
    private TextView textAck, DeviceStatus;
    private Button btnConnect;
    private boolean isLocked = true; // Initial state is locked
    private String deviceInfo;  // Variable to store device information
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private boolean isDeviceConnected = false;  // Track connection state

    private String mParam1;
    private String mParam2;

    public TerminalFragment() {
        // Required empty public constructor
    }

    public static TerminalFragment newInstance(String param1, String param2) {
        TerminalFragment fragment = new TerminalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Retrieve the device info from arguments
        if (getArguments() != null) {
            deviceInfo = getArguments().getString("DEVICE_INFO");
        }

        // Initialize BluetoothAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_devices, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);

        // Initialize views
        buttonSend = view.findViewById(R.id.buttonSend);
        btnUnlock = view.findViewById(R.id.btnUnlock);
        textAck = view.findViewById(R.id.textAck);
        DeviceStatus = view.findViewById(R.id.DeviceStatus);
        btnConnect = view.findViewById(R.id.btnConnect);

        // Set the device information in the DeviceStatus TextView
        if (deviceInfo != null) {
            DeviceStatus.setText(deviceInfo);  // Set the device info here
        } else {
            DeviceStatus.setText("Device info not available");
        }

        // Connect Bluetooth Device and change Button text based on connection status
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnConnect.getText() == "Connect") {
                    // If device is connected, disconnect it
                    btnConnect.setText("Disconnect");

                } else {
                    btnConnect.setText("Connect");
                }

//                if (isDeviceConnected) {
//                    // If device is connected, disconnect it
//                    disconnectDevice();
//                } else {
//                    // If device is not connected, attempt to connect
//                    connectDevice();
//                }
            }
        });

        // Lock button click listener (sets lock state to true and updates status)
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocked = true;
                textAck.setText("Locked");
            }
        });

        // Unlock button click listener (sets lock state to false and updates status)
        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocked = false;
                textAck.setText("Unlocked");
            }
        });

        return view;
    }

    // Method to connect to a Bluetooth device
    private void connectDevice() {
        if (mBluetoothAdapter == null) {
            textAck.setText("Bluetooth not supported on this device.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();  // Turn on Bluetooth if it's not enabled
        }

        // Assuming mBluetoothDevice is a valid BluetoothDevice object
        // In a real-world scenario, you would discover devices and pair with the selected one
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice("DEVICE_ADDRESS");  // Use actual device address

        // Try to connect to the device (Placeholder: Actual connection logic will depend on your implementation)
        // For now, simulate successful connection
        isDeviceConnected = true;

        // Update button text
        btnConnect.setText("Disconnect");

        // Update device status
        DeviceStatus.setText("Device Connected");
    }

    // Method to disconnect from a Bluetooth device
    private void disconnectDevice() {
        // Placeholder: Logic to disconnect from Bluetooth device
        isDeviceConnected = false;

        // Update button text
        btnConnect.setText("Connect");

        // Update device status
        DeviceStatus.setText("Device Disconnected");
    }
}
