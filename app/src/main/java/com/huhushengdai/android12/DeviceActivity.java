package com.huhushengdai.android12;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huhushengdai.tool.log.LogTool;

import java.util.List;

public class DeviceActivity extends AppCompatActivity {

    private BluetoothDevice mBluetoothDevice;
    private TextView mUuidText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Intent i = getIntent();
        String deviceInfo = i.getStringExtra("device");
        mBluetoothDevice = i.getParcelableExtra("BluetoothDevice");
        mUuidText = findViewById(R.id.deviceUuid);
        ((TextView) findViewById(R.id.deviceInfo)).setText(deviceInfo);
    }

    public void createBond(View v) {
        toast("发起配对：" + mBluetoothDevice.createBond());
    }

    public void gattConnect(View v) {
        mBluetoothDevice.connectGatt(this, true, new BluetoothGattCallback() {
            @Override
            public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyUpdate(gatt, txPhy, rxPhy, status);
                LogTool.d("onPhyUpdate");
            }

            @Override
            public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyRead(gatt, txPhy, rxPhy, status);
                LogTool.d("onPhyRead");
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                LogTool.d("onConnectionStateChange old = " + status + ",new = " + newState);
                print(gatt);
                boolean result = gatt.discoverServices();
                LogTool.d("result = " + result);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                LogTool.d("onCharacteristicRead");
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                LogTool.d("onCharacteristicChanged");
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
                LogTool.d("onReadRemoteRssi = " + rssi + ",state = " + status);
            }


            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                LogTool.d("onServicesDiscovered");
                print(gatt);
            }
        });
    }

    public void print(BluetoothGatt gatt) {
        List<BluetoothGattService> services = gatt.getServices();
        if (services == null || services.isEmpty()) {
            runOnUiThread(() -> mUuidText.setText("service is null"));
            return;
        }
        StringBuilder sb = new StringBuilder(32 * services.size());
        int service = 1;
        sb.append("\n");
        for (BluetoothGattService s : services) {
            sb.append(service++).append("-service:").append(s.getUuid()).append("\n");
            List<BluetoothGattCharacteristic> characteristics = s.getCharacteristics();
            if (characteristics == null || characteristics.isEmpty()) {
                continue;
            }
            int i = 1;
            for (BluetoothGattCharacteristic c : characteristics) {
                sb.append(i++).append("-Characteristic:").append(c.getUuid()).append("\n");
            }
        }
        String info = sb.toString();
        LogTool.d(info);
        runOnUiThread(() -> mUuidText.setText(info));
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}