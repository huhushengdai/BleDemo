package com.huhushengdai.android12.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huhushengdai.android12.R;
import com.huhushengdai.android12.bean.ChoiceDevice;
import com.huhushengdai.android12.utils.BluetoothDeviceUtils;
import com.huhushengdai.tool.view.recycler.BaseRecycleAdapter;
import com.huhushengdai.tool.view.recycler.BaseViewHolder;

import java.util.List;

public class DeviceAdapter extends BaseRecycleAdapter<ChoiceDevice> {

    private static final String info = "%s. name = %s\rtype = %s  \nmac = %s  \rstate = %s \n rssi = %s";

    public DeviceAdapter(Context context, List<ChoiceDevice> data) {
        super(context, data);
    }

    @Override
    public void afterCreateViewHolder(BaseViewHolder holder) {
        super.afterCreateViewHolder(holder);
        holder.setClickChild(R.id.itemDeviceContainer);
    }

    @Override
    public void onViewRefresh(@NonNull BaseViewHolder holder, int position) {
        ChoiceDevice choiceDevice = getItemData(position);
        BluetoothDevice device = choiceDevice.device;
        ((TextView) holder.findViewById(R.id.itemDeviceIndex)).setText(choiceDevice.getIndex() + ".");
        ((TextView) holder.findViewById(R.id.itemDeviceName)).setText("name = " + device.getName());
        ((TextView) holder.findViewById(R.id.itemDeviceMac)).setText("mac = " + device.getAddress());
        ((TextView) holder.findViewById(R.id.itemDeviceType)).setText("type = " + BluetoothDeviceUtils.getType(device));
        ((TextView) holder.findViewById(R.id.itemDeviceBondState)).setText("bond state = " + BluetoothDeviceUtils.getState(device));
        ((TextView) holder.findViewById(R.id.itemDeviceRssi)).setText("rssi = " + choiceDevice.getRssi());
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_device;
    }
}
