package com.zxycloud.hzy_xg.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetDeviceListBean;
import com.zxycloud.hzy_xg.bean.GetBean.GetLabelDetailBean;
import com.zxycloud.hzy_xg.bean.GetBean.GetPlanDetailBean;
import com.zxycloud.hzy_xg.bean.base.DeviceBean;
import com.zxycloud.hzy_xg.bean.base.LabelBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.ui.adapter.LabelItemAdapter;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.TxtUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zxycloud.hzy_xg.BuildConfig.labelDetail;

public class LabelDetailsActivity extends BaseNetActivity {
    TextView tv_label_name,tv_label_num,tv_add_time,tv_install_address;
    RecyclerView recyclerView;
    private String markId;
    private LabelItemAdapter labelItemAdapter;

    @Override
    protected void findViews() {
        recyclerView = getView(R.id.recyclerView);
        tv_label_name = getView(R.id.tv_label_name);
        tv_label_num = getView(R.id.tv_label_num);
        tv_add_time = getView(R.id.tv_add_time);
        tv_install_address = getView(R.id.tv_install_address);
        labelItemAdapter = new LabelItemAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(labelItemAdapter);
        labelItemAdapter.setDeviecInterface(new LabelItemAdapter.DeviecInterface() {
            @Override
            public void setJumb(String deviceId) {
                Bundle bundle = new Bundle();
                bundle.putString("deviceId", deviceId);
                jumpTo(DeviceDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.label_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        params = new HashMap<>();
        params.put("markId", markId);
        get(BuildConfig.labelDetail, GetLabelDetailBean.class, false, NetUtils.PATROL);
        params = new HashMap<>();
        params.put("markId", markId);
        params.put("pageNumber", 1);
        params.put("pageSize", 100);
        get(BuildConfig.taskDeviceList, GetDeviceListBean.class, false, NetUtils.PATROL);
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            markId = bundle.getString("markId");
        }
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.labelDetail:
                if (baseBean.isSuccessful()) {
                    LabelBean labelBean = ((GetLabelDetailBean) baseBean).getData();
                    if (labelBean != null){
                        tv_label_name.setText("标签名称：".concat(TxtUtils.getText(labelBean.getName())));
                        tv_label_num.setText("标签编号：".concat(TxtUtils.getText(labelBean.getId())));
                        tv_add_time.setText("添加时间：".concat(TxtUtils.getText(labelBean.getAddTime())));
                        tv_install_address.setText("安装位置：".concat(TxtUtils.getText(labelBean.getAddressDescribation())));
                    }
                }else {
                    toast(baseBean.getMessage());
                }
                break;
            case BuildConfig.taskDeviceList:
                if (baseBean.isSuccessful()) {
                    List<DeviceBean> deviceBeans = ((GetDeviceListBean) baseBean).getData();
                    if (deviceBeans != null){
                        labelItemAdapter.setData(deviceBeans);
                    }else {
                        toast(baseBean.getMessage());
                    }
                }else {
                    toast(baseBean.getMessage());
                }
                break;
        }
    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {
        if (errorString.equals(NO_NET_TAG)) {
            noNet();
        } else {
            toast(errorString);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_label_details;
    }

    @Override
    protected void formatData() {

    }
}
