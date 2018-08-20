package com.zxycloud.hzy_xg.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxycloud.hzy_xg.R;

public class LabelHolder extends ViewHolder{
    public TextView tabel_name,tabel_state;
    public ImageView delete_img;
    public Button rl_background;

    public LabelHolder(View itemView) {
        super(itemView);
        tabel_name = itemView.findViewById(R.id.tabel_name);
        tabel_state = itemView.findViewById(R.id.tabel_state);
        delete_img = itemView.findViewById(R.id.delete_img);
        rl_background = itemView.findViewById(R.id.rl_background);
    }
}
