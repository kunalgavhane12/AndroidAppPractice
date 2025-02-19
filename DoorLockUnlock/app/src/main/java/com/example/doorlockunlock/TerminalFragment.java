package com.example.doorlockunlock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TerminalFragment extends Fragment {

    public TerminalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);

        // Find Views
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        Button btnLock = view.findViewById(R.id.btnLock);
        Button btnUnlock = view.findViewById(R.id.btnUnlock);

        // Set Button Click Listeners
        btnLock.setOnClickListener(v -> txtStatus.setText("Locked"));
        btnUnlock.setOnClickListener(v -> txtStatus.setText("Unlocked"));

        return view;
    }
}
