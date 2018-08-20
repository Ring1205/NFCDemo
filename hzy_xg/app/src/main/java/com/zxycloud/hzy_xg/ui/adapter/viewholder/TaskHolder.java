package com.zxycloud.hzy_xg.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;

public class TaskHolder extends RecyclerView.ViewHolder {
    public TextView tv_task_name,tv_label_name,tv_time_interval,tv_task_style,tv_task_state;
    public TaskHolder(View itemView) {
        super(itemView);
        tv_time_interval = itemView.findViewById(R.id.tv_time_interval);
        tv_label_name = itemView.findViewById(R.id.tv_label_name);
        tv_task_name = itemView.findViewById(R.id.tv_task_name);
        tv_task_style = itemView.findViewById(R.id.tv_task_style);
        tv_task_state = itemView.findViewById(R.id.tv_task_state);
    }
}
