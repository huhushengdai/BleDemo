package com.huhushengdai.android12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.huhushengdai.android12.utils.AdvertiseUtils;

public class SendAdvertiseActivity extends AppCompatActivity {

    private AdvertiseUtils mAdvertiseUtils;
    private EditText mAdvertiseDataEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_advertise);
        mAdvertiseDataEdit = findViewById(R.id.advertiseData);
        mAdvertiseUtils = new AdvertiseUtils();
        mAdvertiseUtils.init(this);
    }

    public void sendAdvertise(View v){
        String data = mAdvertiseDataEdit.getText().toString();
        mAdvertiseUtils.send(0x1111,TextUtils.isEmpty(data)?new byte[]{}:data.getBytes());
    }

    public void stopAdvertise(View v){
        mAdvertiseUtils.stop();
    }
}