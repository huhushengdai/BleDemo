package com.huhushengdai.android12;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Toast;

import com.huhushengdai.android12.adapter.DeviceAdapter;
import com.huhushengdai.android12.bean.ChoiceDevice;
import com.huhushengdai.android12.utils.BluetoothDeviceUtils;
import com.huhushengdai.tool.log.LogTool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class BondListActivity extends AppCompatActivity {

    private DeviceAdapter mAdapter;
    private final ArrayList<ChoiceDevice> mDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bond_list);
        RecyclerView r = findViewById(R.id.bondList);
        r.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DeviceAdapter(this, mDevices);
        r.setAdapter(mAdapter);
        mAdapter.setOnChildClickListener((parent, child, position) -> toDevice(mAdapter.getItemData(position).device));
    }

    public void getBondList(View v) {
        Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        mDevices.clear();
        if (devices != null && !devices.isEmpty()) {
            int index = 1;
            for (BluetoothDevice device : devices) {
                mDevices.add(new ChoiceDevice(device, (short) 0, index++));
            }
        }
        if (mDevices.isEmpty()){
            Toast.makeText(this, "没有已配对设备", Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
    }

    public void toDevice(BluetoothDevice device) {
        StringBuilder sb = new StringBuilder(64);
        String bondState = BluetoothDeviceUtils.getState(device);
        String type = BluetoothDeviceUtils.getType(device);
        sb.append("device mac = ").append(device.getAddress()).append("\n")
                .append("name = ").append(device.getName()).append("\n")
                .append("state = ").append(bondState).append("\n")
                .append("type = ").append(type).append("\n");

        ParcelUuid[] uuids = device.getUuids();
        if (uuids == null) {
            sb.append("没有uuid");
        } else {
            sb.append("uuid:").append("\n");
            for (ParcelUuid uuid : uuids) {
                sb.append(uuid.toString()).append("\n");
            }
        }
        Intent i = new Intent(this, DeviceActivity.class);
        LogTool.d(sb.toString());
        i.putExtra("device", sb.toString());
        i.putExtra("BluetoothDevice", device);
        startActivity(i);
    }
}