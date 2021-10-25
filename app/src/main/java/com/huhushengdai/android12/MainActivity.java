package com.huhushengdai.android12;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat mBluetoothSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothSwitch = findViewById(R.id.bluetoothSwitch);
        mBluetoothSwitch.setOnCheckedChangeListener((compoundButton,isCheck)-> {
            if (isCheck){
                BluetoothAdapter.getDefaultAdapter().enable();
            }else {
                BluetoothAdapter.getDefaultAdapter().disable();
            }
        });

        mBluetoothSwitch.setChecked(BluetoothAdapter.getDefaultAdapter().isEnabled());
    }

    /**
     * 权限申请
     */
    public void requestPermissions(View v) {
        toActivity(RequestPermissionsActivity.class);
    }

    /**
     * 去扫描
     */
    public void toScan(View v) {
        toActivity(ScanActivity.class);
    }

    /**
     * 已配对列表
     */
    public void toBondList(View v) {
        toActivity(BondListActivity.class);
    }

    /**
     * 去发送蓝牙广播界面
     */
    public void toSendAdvertise(View v){
        toActivity(SendAdvertiseActivity.class);
    }

    private void toActivity(Class<?> cls){
        startActivity(new Intent(this, cls));
    }
}