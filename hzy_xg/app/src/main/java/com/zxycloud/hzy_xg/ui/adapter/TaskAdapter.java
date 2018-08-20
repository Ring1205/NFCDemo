package com.zxycloud.hzy_xg.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.activity.BaseActivity;
import com.zxycloud.hzy_xg.bean.base.PlanBean;
import com.zxycloud.hzy_xg.bean.base.TaskBean;
import com.zxycloud.hzy_xg.ui.activity.TaskDetailsActivity;
import com.zxycloud.hzy_xg.ui.adapter.viewholder.PlanHolder;
import com.zxycloud.hzy_xg.ui.adapter.viewholder.TaskHolder;
import com.zxycloud.hzy_xg.utils.TxtUtils;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
    Context context;
    String type;
    List<TaskBean> data;

    public TaskAdapter(Context context) {
        this.context = context;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(List<TaskBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(View.inflate(context, R.layout.item_task, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        final TaskBean bean = data.get(position);
        holder.tv_task_name.setText("任务名称：".concat(bean.getTaskName()));
        holder.tv_label_name.setText("关联标签：".concat(bean.getMarkName()));
        holder.tv_task_style.setText("任务反馈：".concat(bean.getStatusName()));
        if ("正常".equals(bean.getStatusName())) {
            TxtUtils.setTextColorForeground(holder.tv_task_style, context.getResources().getColor(R.color.green), 5, holder.tv_task_style.getText().toString().length());
        } else if ("不正常".equals(bean.getStatusName())) {
            TxtUtils.setTextColorForeground(holder.tv_task_style, context.getResources().getColor(R.color.red), 5, holder.tv_task_style.getText().toString().length());
        }

        if (type != null)
            holder.tv_task_state.setVisibility(View.VISIBLE);
        else
            holder.tv_task_state.setVisibility(View.GONE);

        holder.tv_task_state.setText(TxtUtils.getText(bean.getPatrolName()));
        int color = context.getResources().getColor(R.color.text_color);
        switch (bean.getPatrolName()) {
            case "进行中":
                color = context.getResources().getColor(R.color.orange);
                break;
            case "未开始":
                color = context.getResources().getColor(R.color.green);
                break;
            case "已完成":
                color = context.getResources().getColor(R.color.text_gray_color);
                break;
            case "已过期":
                color = context.getResources().getColor(R.color.text_gray_color);
                break;
        }
        TxtUtils.setTextColorForeground(holder.tv_task_state, color, 0, holder.tv_task_state.getText().toString().length());
        holder.tv_time_interval.setText(bean.getStartTime().concat(" ~ ").concat(bean.getEndTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskInterfacer.getType(bean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    TaskInterfacer taskInterfacer;

    public void setTaskInterfacer(TaskInterfacer taskInterfacer) {
        this.taskInterfacer = taskInterfacer;
    }

    public interface TaskInterfacer {
        void getType(String taskId);
    }
}
