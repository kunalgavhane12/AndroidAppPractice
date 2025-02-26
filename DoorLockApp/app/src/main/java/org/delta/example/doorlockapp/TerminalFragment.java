package org.delta.example.doorlockapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
public class TerminalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageButton buttonSend, btnUnlock;
    private TextView textAck, DeviceStatus;
    private boolean isLocked = true; // Initial state is locked
    private String deviceInfo;  // Variable to store device information

    // TODO: Rename and change types of parameters
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

        // Set the device information in the DeviceStatus TextView
        if (deviceInfo != null) {
            DeviceStatus.setText(deviceInfo);  // Set the device info here
        } else {
            DeviceStatus.setText("Device info not available");
        }

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
}
