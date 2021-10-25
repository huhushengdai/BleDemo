package com.huhushengdai.android12.bean;

import android.bluetooth.BluetoothDevice;

public class ChoiceDevice {
    private boolean check;
    public final BluetoothDevice device;
    private short rssi;
    private int index;

    public ChoiceDevice(BluetoothDevice device, short rssi,int index) {
        this.device = device;
        this.rssi = rssi;
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public short getRssi() {
        return rssi;
    }

    public void setRssi(short rssi) {
        this.rssi = rssi;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
