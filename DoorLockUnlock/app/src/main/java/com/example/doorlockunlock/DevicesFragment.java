package com.example.doorlockunlock;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DevicesFragment extends ListFragment {
    static class ListItem {
        UsbDevice device;
        int port;
        UsbSerialDriver driver;

        ListItem(UsbDevice device, int port, UsbSerialDriver driver) {
            this.device = device;
            this.port = port;
            this.driver = driver;
        }
    }
    private final ArrayList<ListItem> listItems = new ArrayList<>();
    private ArrayAdapter<ListItem> listAdapter;
    private int baudRate = 115200;
    private boolean withIoManager = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listAdapter = new ArrayAdapter<ListItem>(getActivity(), 0, listItems){
            public View getView(int position, View view, ViewGroup parent){
                ListItem item = listItems.get(position);
                if(view == null)
                    view = getActivity().getLayoutInflater().inflate(R.layout.device_list_item, parent, false);
                TextView text1 = view.findViewById(R.id.text1);
                TextView text2 = view.findViewById(R.id.text2);
                if(item.driver == null) {
                    text1.setText("<no driver>");
                } else if (item.driver.getPorts().size() == 1) {
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", ""));
                } else {
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", "")+", Port "+ item.port);
                }
                text2.setText(String.format(Locale.US, "Vendor %04X, Product %04X", item.device.getDeviceId(), item.device.getProductId()));
                return view;
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        View mainAct = getActivity().getLayoutInflater().inflate(R.layout.activity_main, null, false);
        getListView().addHeaderView(mainAct, null, false);
        setEmptyText("<no USB devices found>");
        ((TextView) getListView().getEmptyView()).setTextSize(18);
        setListAdapter(listAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
    void refresh() {
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        listItems.clear();
        for(UsbDevice device: usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if (driver != null) {
                for (int port = 0; port < driver.getPorts().size(); port++) {
                    listItems.add(new ListItem(device, port, driver));
                }
            } else {
                listItems.add(new ListItem(device, 0, null));
            }
        }
        listAdapter.notifyDataSetChanged();
    }
}
