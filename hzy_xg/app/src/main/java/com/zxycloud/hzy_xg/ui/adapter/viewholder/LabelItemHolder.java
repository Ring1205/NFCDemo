package com.zxycloud.hzy_xg.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;

public class LabelItemHolder extends RecyclerView.ViewHolder {
    public TextView tv_device_state,tv_device_name;
    public LabelItemHolder(View itemView) {
        super(itemView);
        tv_device_state = itemView.findViewById(R.id.tv_device_state);
        tv_device_name = itemView.findViewById(R.id.tv_device_name);
    }
}
