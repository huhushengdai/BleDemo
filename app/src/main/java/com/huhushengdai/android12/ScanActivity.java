package com.huhushengdai.android12;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.ParcelUuid;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huhushengdai.android12.adapter.DeviceAdapter;
import com.huhushengdai.android12.bean.ChoiceDevice;
import com.huhushengdai.android12.databinding.ActivityScanBinding;
import com.huhushengdai.android12.utils.BluetoothDeviceUtils;
import com.huhushengdai.tool.log.LogTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScanActivity extends AppCompatActivity {

    private final ArrayList<ChoiceDevice> devices = new ArrayList<>();
    private DeviceAdapter mAdapter;
    private EditText mFilterNameEdit;
    private EditText mFilterMacEdit;
    private int mIndex;
    private ScanCallback callback;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogTool.d("onReceive = " + action);
            // 当 Discovery 发现了一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 从 Intent 中获取发现的 BluetoothDevice

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onFoundDevice(device, null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        RecyclerView r = findViewById(R.id.scanDeviceList);
        r.setLayoutManager(new LinearLayoutManager(this));
        mFilterNameEdit = findViewById(R.id.scanNameFilter);
        mFilterMacEdit = findViewById(R.id.scanMacFilter);
        mAdapter = new DeviceAdapter(this, devices);
        mAdapter.setOnChildClickListener((parent, child, position) -> toDevice(mAdapter.getItemData(position).device));
        r.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScan(null);
        unregisterReceiver(mReceiver);
    }

    public void btScan(View view) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()){
            toast("蓝牙没开启，请手动开启");
            return;
        }
        LogTool.d("btScan");
        toast("开启经典蓝牙扫描结果 ：" + adapter.startDiscovery());
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void bleScan(View view) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()){
            toast("蓝牙没开启，请手动开启");
            return;
        }

        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        ScanFilter scanFilter = new ScanFilter.Builder().build();
        filters.add(scanFilter);
        ScanSettings settings = new ScanSettings.Builder().setCallbackType(
                ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
                .build();

        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (scanner == null) {
            toast("scanner is null,应该是没有权限");
            return;
        }
        if (callback != null) {
            scanner.stopScan(callback);
        }
        callback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                onFoundDevice(result.getDevice(), result.getScanRecord().getBytes());
            }
        };
        scanner.startScan(null, settings, callback);
        LogTool.d("BLEScan");
        toast("BLE扫描");
    }

    public void stopScan(View view) {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        BluetoothLeScanner scanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        if (scanner != null) {
            scanner.stopScan(callback);
        }
    }

    public void clearScan(View view) {
        //todo 这里列表data 写法有点问题，后续改下
//        devices.clear();
        mAdapter.setData(null);
        mAdapter.notifyDataSetChanged();
    }

    private void onFoundDevice(BluetoothDevice device, byte[] record) {
        String type = BluetoothDeviceUtils.getType(device);
        LogTool.i("mac = " + device.getAddress() + ",type = "
                + type + ",name " + device.getName()
                + "\n" + (record == null || record.length <= 0 ? "is null" : bytesToHexString(record))
        );
        if (!isAddList(device)) {
            return;
        }
        if (devices.size() >= 100) {
            devices.remove(0);
        }
        devices.add(new ChoiceDevice(device, (short) 0, mIndex++));
        ArrayList<ChoiceDevice> tempList = new ArrayList<>(devices);
        Collections.reverse(tempList);
        mAdapter.setData(tempList);
        mAdapter.notifyDataSetChanged();
    }

    private boolean isAddList(BluetoothDevice device) {
        if (device == null) {
            return false;
        }
        for (ChoiceDevice choiceDevice : devices) {
            if (Objects.equals(choiceDevice.device.getAddress(), device.getAddress())) {
                return false;
            }
        }
        return matchingName(device.getName()) && matchingMac(device.getAddress());
    }

    private boolean matchingName(String deviceName) {
        return matchingHandler(mFilterNameEdit, deviceName);
    }

    private boolean matchingMac(String mac) {
        return matchingHandler(mFilterMacEdit, mac);
    }

    private boolean matchingHandler(EditText editText, String matchingStr) {
        String filter = editText.getText().toString();
        if (TextUtils.isEmpty(filter)) {
            return true;
        }
        return !TextUtils.isEmpty(matchingStr)
                && (matchingStr.toUpperCase().contains(filter.toUpperCase()));
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src != null && src.length > 0) {
            for (int i = 0; i < src.length; ++i) {
                int v = src[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    /**
     * 跳转到蓝牙详情
     */
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