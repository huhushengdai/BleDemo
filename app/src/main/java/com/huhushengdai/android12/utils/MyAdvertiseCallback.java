package com.huhushengdai.android12.utils;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;

import com.huhushengdai.tool.log.LogTool;

/**
 * @author xuxing
 * date：2021/9/24 4:10 下午
 * description：
 */
public class MyAdvertiseCallback extends AdvertiseCallback {

    @Override
    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
        super.onStartSuccess(settingsInEffect);
        if (settingsInEffect != null) {
            LogTool.d("onStartSuccess TxPowerLv=" + settingsInEffect.getTxPowerLevel() + " mode=" + settingsInEffect.getMode()
                    + " timeout=" + settingsInEffect.getTimeout());
        } else {
            LogTool.e("onStartSuccess, settingInEffect is null");
        }
        LogTool.e("onStartSuccess settingsInEffect" + settingsInEffect);
//            mBluetoothLeAdvertiser.stopAdvertising(this);
//            mBluetoothLeAdvertiser.stopAdvertising(MyAdvertiseCallback.this);
//            mHandler.postDelayed(() -> {
//                        mBluetoothLeAdvertiser.stopAdvertising(MyAdvertiseCallback.this);
//                        Toast.makeText(MainActivity.this,"停止",Toast.LENGTH_SHORT).show();
//                    }, 900);
    }

    @Override
    public void onStartFailure(int errorCode) {
        super.onStartFailure(errorCode);
        LogTool.e("onStartFailure errorCode" + errorCode);

        if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
            LogTool.e("Failed to start advertising as the advertise data to be broadcasted is larger than 31 bytes.");
        } else if (errorCode == ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
            LogTool.e("Failed to start advertising because no advertising instance is available.");
        } else if (errorCode == ADVERTISE_FAILED_ALREADY_STARTED) {
            LogTool.e("Failed to start advertising as the advertising is already started");
        } else if (errorCode == ADVERTISE_FAILED_INTERNAL_ERROR) {
            LogTool.e("Operation failed due to an internal error");
        } else if (errorCode == ADVERTISE_FAILED_FEATURE_UNSUPPORTED) {
            LogTool.e("This feature is not supported on this platform");
        }
    }
}
