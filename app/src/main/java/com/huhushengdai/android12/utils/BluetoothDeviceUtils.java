package com.huhushengdai.android12.utils;

import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceUtils {
    public static String getState(BluetoothDevice device){
        String bondState;
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_NONE:
                bondState = "none";
                break;
            case BluetoothDevice.BOND_BONDING:
                bondState = "bonding";
                break;
            case BluetoothDevice.BOND_BONDED:
                bondState = "bonded";
                break;
            default:
                bondState = "unkown";
                break;
        }
        return bondState;
    }
    public static String getType(BluetoothDevice device){
        String type;
        switch (device.getType()) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                type = "classic";
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                type = "le";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                type = "dual";
                break;
            default:
                type = "unkown";
                break;
        }
        return type;
    }
}
