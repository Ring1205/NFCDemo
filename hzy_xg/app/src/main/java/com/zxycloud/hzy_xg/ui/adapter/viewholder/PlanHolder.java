package com.zxycloud.hzy_xg.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;

public class PlanHolder extends RecyclerView.ViewHolder {
    public TextView tv_plan_name,tv_plan_type,tv_create_time;
    public PlanHolder(View itemView) {
        super(itemView);
        tv_create_time = itemView.findViewById(R.id.tv_create_time);
        tv_plan_type = itemView.findViewById(R.id.tv_plan_type);
        tv_plan_name = itemView.findViewById(R.id.tv_plan_name);
    }
}
