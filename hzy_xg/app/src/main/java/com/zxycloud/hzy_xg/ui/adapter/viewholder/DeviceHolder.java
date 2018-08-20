package com.zxycloud.hzy_xg.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;

public class DeviceHolder extends RecyclerView.ViewHolder {
    public ImageView iv_state;
    public TextView tv_device_address,tv_checkbox,tv_device_name,tv_device_state;
    public CheckBox checkbox;
    public Button rl_background;

    public DeviceHolder(View itemView) {
        super(itemView);
        iv_state = itemView.findViewById(R.id.iv_state);
        tv_device_address = itemView.findViewById(R.id.tv_device_address);
        tv_checkbox = itemView.findViewById(R.id.tv_checkbox);
        tv_device_name = itemView.findViewById(R.id.tv_device_name);
        tv_device_state = itemView.findViewById(R.id.tv_device_state);
        checkbox = itemView.findViewById(R.id.checkbox);
        rl_background = itemView.findViewById(R.id.rl_background);
    }
}
