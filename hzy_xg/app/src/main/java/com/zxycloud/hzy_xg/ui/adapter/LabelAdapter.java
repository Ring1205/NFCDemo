package com.zxycloud.hzy_xg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.bean.GetBean.GetLabelBean;
import com.zxycloud.hzy_xg.bean.base.LabelBean;
import com.zxycloud.hzy_xg.ui.adapter.viewholder.LabelHolder;

import java.util.List;

public class LabelAdapter extends RecyclerView.Adapter<LabelHolder> {
    Context context;
    List<LabelBean> data;
    public LabelAdapter(Context context){
        this.context = context;
    }
    public void getData(List<LabelBean> data){
        this.data = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public LabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LabelHolder(View.inflate(context, R.layout.tabel_item_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull LabelHolder holder, int position) {
        final LabelBean bean = data.get(position);
        holder.tabel_name.setText("标签名：".concat(bean.getName()));
        holder.tabel_state.setText("添加时间：".concat(bean.getAddTime()));
        holder.delete_img.setTag(position);
        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteabelInterface.delete((Integer) view.getTag());
            }
        });
        holder.rl_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteabelInterface.jumb(bean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
    DeleteabelInterface deleteabelInterface;
    public void setDeleteabelInterface(DeleteabelInterface deleteabelInterface){
        this.deleteabelInterface = deleteabelInterface;
    }
    public interface DeleteabelInterface{
        void delete(int position);
        void jumb(String markId);
    }
}
