package com.huhushengdai.android12;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class RequestPermissionsActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_CODE = 102;

    private CheckBox mBluetoothAdminCheck;
    private CheckBox mBluetoothCheck;
    private CheckBox mAccessFineLocationCheck;
    private CheckBox mAccessCoarseLocationCheck;
    private CheckBox mBluetoothScanCheck;
    private CheckBox mBluetoothAdvertiseCheck;
    private CheckBox mBluetoothConnectCheck;
    private Button mRequestPermissions;

    private CompoundButton.OnCheckedChangeListener mCheckListener = (compoundButton, isCheck) -> {
        boolean result = mBluetoothAdminCheck.isChecked() || mBluetoothCheck.isChecked()
                || mAccessFineLocationCheck.isChecked() || mAccessCoarseLocationCheck.isChecked()
                || mBluetoothScanCheck.isChecked() || mBluetoothAdvertiseCheck.isChecked() ||
                mBluetoothConnectCheck.isChecked();
        mRequestPermissions.setClickable(result);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);
        mBluetoothAdminCheck = findViewById(R.id.bluetoothAdmin);
        mBluetoothCheck = findViewById(R.id.bluetooth);
        mAccessFineLocationCheck = findViewById(R.id.accessFineLocation);
        mAccessCoarseLocationCheck = findViewById(R.id.accessCoarseLocation);
        mBluetoothScanCheck = findViewById(R.id.bluetoothScan);
        mBluetoothAdvertiseCheck = findViewById(R.id.bluetoothAdvertise);
        mBluetoothConnectCheck = findViewById(R.id.bluetoothConnect);
        mRequestPermissions = findViewById(R.id.requestPermissions);

        mBluetoothAdminCheck.setOnCheckedChangeListener(mCheckListener);
        mBluetoothCheck.setOnCheckedChangeListener(mCheckListener);
        mAccessFineLocationCheck.setOnCheckedChangeListener(mCheckListener);
        mAccessCoarseLocationCheck.setOnCheckedChangeListener(mCheckListener);
        mBluetoothScanCheck.setOnCheckedChangeListener(mCheckListener);
        mBluetoothAdvertiseCheck.setOnCheckedChangeListener(mCheckListener);
        mBluetoothConnectCheck.setOnCheckedChangeListener(mCheckListener);

        mRequestPermissions.setOnClickListener((v) -> requestPermissions());

        checkPermissions();
    }

    private void requestPermissions() {
        ArrayList<String> permissions = new ArrayList<>();
        addPermissions(permissions, mBluetoothAdminCheck, Manifest.permission.BLUETOOTH_ADMIN);
        addPermissions(permissions, mBluetoothCheck, Manifest.permission.BLUETOOTH);
        addPermissions(permissions, mAccessFineLocationCheck, Manifest.permission.ACCESS_FINE_LOCATION);
        addPermissions(permissions, mAccessCoarseLocationCheck, Manifest.permission.ACCESS_COARSE_LOCATION);

        addPermissions(permissions, mBluetoothScanCheck, Manifest.permission.BLUETOOTH_SCAN);
        addPermissions(permissions, mBluetoothAdvertiseCheck, Manifest.permission.BLUETOOTH_ADVERTISE);
        addPermissions(permissions, mBluetoothConnectCheck, Manifest.permission.BLUETOOTH_CONNECT);
        String[] requestPermissions = new String[permissions.size()];
        requestPermissions = permissions.toArray(requestPermissions);
        ActivityCompat.requestPermissions(this, requestPermissions, REQUEST_PERMISSIONS_CODE);
    }

    private void addPermissions(List<String> permissions, CheckBox v, String p) {
        if (v.isChecked() && v.isEnabled()) {
            permissions.add(p);
        }
    }

    private void checkPermissions() {
        checkAndBindView(mBluetoothAdminCheck, Manifest.permission.BLUETOOTH_ADMIN);
        checkAndBindView(mBluetoothCheck, Manifest.permission.BLUETOOTH);
        checkAndBindView(mAccessFineLocationCheck, Manifest.permission.ACCESS_FINE_LOCATION);
        checkAndBindView(mAccessCoarseLocationCheck, Manifest.permission.ACCESS_COARSE_LOCATION);
        checkAndBindView(mBluetoothScanCheck, Manifest.permission.BLUETOOTH_SCAN);
        checkAndBindView(mBluetoothAdvertiseCheck, Manifest.permission.BLUETOOTH_ADVERTISE);
        checkAndBindView(mBluetoothConnectCheck, Manifest.permission.BLUETOOTH_CONNECT);
    }

    private void checkAndBindView(CheckBox v, String permission) {
        boolean OK = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (OK) {
            v.setEnabled(false);
            v.setChecked(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_PERMISSIONS_CODE) {
            return;
        }
        checkPermissions();
        Toast.makeText(this, "权限申请完成", Toast.LENGTH_SHORT).show();
    }
}