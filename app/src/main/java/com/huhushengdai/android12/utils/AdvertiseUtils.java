package com.huhushengdai.android12.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.huhushengdai.tool.log.LogTool;

/**
 * @author xuxing
 * date：2021/9/24 4:09 下午
 * description：
 */
public class AdvertiseUtils {

    private AdvertiseCallback mAdvertiseCallback;
    private BluetoothManager mBluetoothManager;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    public void init(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "不支持BLE", Toast.LENGTH_LONG).show();
            return;
        }
        mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, "蓝牙不支持", Toast.LENGTH_LONG).show();
            return;
        }
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (mBluetoothLeAdvertiser == null) {
            Toast.makeText(context, "the device not support peripheral", Toast.LENGTH_SHORT).show();
        }
    }

    public void send(int manufacturerId, byte[] byteData) {
        if (mAdvertiseCallback != null) {
            stop();
        }
        mAdvertiseCallback = new MyAdvertiseCallback();
        AdvertiseData data = createAdvertiseData(manufacturerId, byteData);
        mBluetoothLeAdvertiser.startAdvertising(
                createAdvSettings(false, 0),
                data, mAdvertiseCallback);
    }

    public void stop() {
        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
    }

    //广播数据
    public AdvertiseData createAdvertiseData(int manufacturerId, byte[] data) {
        LogTool.d("data size = " + data.length);
        AdvertiseData.Builder mDataBuilder = new AdvertiseData.Builder();
        mDataBuilder.setIncludeDeviceName(true); //广播名称也需要字节长度
        mDataBuilder.addManufacturerData(manufacturerId,
                data);
        AdvertiseData mAdvertiseData = mDataBuilder.build();
        if (mAdvertiseData == null) {
            LogTool.e("mAdvertiseSettings == null");
        }
        return mAdvertiseData;
    }


    /**
     * 广播的一些基本设置
     **/
    public AdvertiseSettings createAdvSettings(boolean connectAble, int timeoutMillis) {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        //
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        builder.setConnectable(connectAble);
        builder.setTimeout(timeoutMillis);
        AdvertiseSettings mAdvertiseSettings = builder.build();
        if (mAdvertiseSettings == null) {
            LogTool.e("mAdvertiseSettings == null");
        }
        return mAdvertiseSettings;
    }
}
