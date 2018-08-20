package com.zxycloud.hzy_xg.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.bean.base.PlanBean;
import com.zxycloud.hzy_xg.ui.adapter.viewholder.PlanHolder;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanHolder> {
    Context context;
    List<PlanBean> data;

    public PlanAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<PlanBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanHolder(View.inflate(context, R.layout.item_plan, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PlanHolder holder, int position) {
        final PlanBean bean = data.get(position);
        holder.tv_create_time.setText("创建日期：".concat(bean.getCreateTime()));
        holder.tv_plan_name.setText("计划标题：".concat(bean.getTitle()));
        switch (bean.getGenTaskType()) {
            case 1:
                holder.tv_plan_type.setText("类型：每日巡检");
                break;
            case 2:
                holder.tv_plan_type.setText("类型：每周巡检");
                break;
            case 3:
                holder.tv_plan_type.setText("类型：每月巡检");
                break;
            case 4:
                holder.tv_plan_type.setText("类型：季度巡检");
                break;
            case 5:
                holder.tv_plan_type.setText("类型：每年巡检");
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                planInterface.setPlanId(bean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    PlanInterface planInterface;
    public void setPlanInterface(PlanInterface planInterface){
        this.planInterface = planInterface;
    }
    public interface PlanInterface{
        void setPlanId(String planId);
    }
}
