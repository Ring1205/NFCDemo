package com.zxycloud.hzy_xg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.bean.base.DeviceBean;
import com.zxycloud.hzy_xg.ui.adapter.viewholder.LabelItemHolder;

import java.util.List;

public class LabelItemAdapter extends RecyclerView.Adapter<LabelItemHolder>{
    private Context context;
    private List<DeviceBean> data;

    public LabelItemAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DeviceBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LabelItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LabelItemHolder(View.inflate(context, R.layout.item_label,null));
    }

    @Override
    public void onBindViewHolder(@NonNull LabelItemHolder holder, int position) {
        final DeviceBean bean = data.get(position);
        holder.tv_device_name.setText("设备名称：".concat(bean.getDeviceName()));
        holder.tv_device_state.setText("设备编号：".concat(bean.getDeviceNumber()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviecInterface.setJumb(bean.getDeviceGuid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
    DeviecInterface deviecInterface;
    public void setDeviecInterface(DeviecInterface deviecInterface){
        this.deviecInterface = deviecInterface;
    }
    public interface DeviecInterface{
        void setJumb(String deviceId);
    }
}
