package com.zxycloud.hzy_xg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.bean.base.DeviceBean;
import com.zxycloud.hzy_xg.ui.adapter.viewholder.DeviceHolder;
import com.zxycloud.hzy_xg.utils.GlideUtils;

import java.util.List;

public class DeviecAdapter extends RecyclerView.Adapter<DeviceHolder> {
    private Context context;
    private String type;
    private List<DeviceBean> data;

    public DeviecAdapter(Context context,String type) {
        this.context = context;
        this.type = type;
    }

    public void setData(List<DeviceBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<DeviceBean> getData(){
        return data;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceHolder(View.inflate(context, R.layout.item_device, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceHolder holder, final int position) {
        final DeviceBean bean = data.get(position);
        if (type != null && !type.isEmpty()){
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.tv_checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        bean.setCheck(b);
                        holder.tv_checkbox.setText("已检查");
                    }else {
                        bean.setCheck(b);
                        holder.tv_checkbox.setText("未检查");
                    }
                    data.remove(position);
                    data.add(position,bean);
                }
            });
        }else {
            holder.checkbox.setVisibility(View.INVISIBLE);
            holder.tv_checkbox.setVisibility(View.INVISIBLE);
        }
        if (bean.getDeviceState() == 1){
            holder.iv_state.setImageResource(R.mipmap.alert_normal);
        }else if (bean.getDeviceState() == 2){
            holder.iv_state.setImageResource(R.mipmap.alert_trouble);
        }
        holder.rl_background.setTag(holder.checkbox);
        holder.tv_device_name.setText("设备名称：".concat(bean.getDeviceName()));
        holder.tv_device_state.setText("设备状态：".concat(bean.getDeviceStateName()));
        holder.tv_device_address.setText("安装位置：".concat(bean.getInstalllocation()));
        holder.rl_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviecInterface.setJumb(bean.getDeviceGuid());
                CheckBox checkBox = (CheckBox) view.getTag();
                checkBox.setChecked(true);
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
