package com.zxycloud.hzy_xg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxycloud.hzy_xg.R;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class TabView extends LinearLayout {

    private ImageView ivTab;

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.tab_view, this);
        ivTab = (ImageView) findViewById(R.id.img_tab);
    }

    public void setData(int resourcesId) {
        ivTab.setBackgroundResource(resourcesId);
    }

    public void setData(String name, int resourcesId) {
        ivTab.setBackgroundResource(resourcesId);
    }

    public void setData(int nameId, int resourcesId) {
        ivTab.setBackgroundResource(resourcesId);
    }
}
