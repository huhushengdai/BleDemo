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

    private static final String info = "%s. name = %s \nmac = %s \ntype = %s  \nstate = %s";

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
        ((TextView) holder.findViewById(R.id.itemDeviceInfo))
                .setText(String.format(info, choiceDevice.getIndex(),
                        device.getName()
                        , device.getAddress()
                        , BluetoothDeviceUtils.getType(device)
                        , BluetoothDeviceUtils.getState(device)));
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_device;
    }
}
