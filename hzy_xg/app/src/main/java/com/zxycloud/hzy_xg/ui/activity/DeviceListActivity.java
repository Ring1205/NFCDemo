package com.zxycloud.hzy_xg.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetDeviceListBean;
import com.zxycloud.hzy_xg.bean.base.DeviceBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.ui.adapter.DeviecAdapter;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog.NO_NET_TAG;

public class DeviceListActivity extends BaseNetActivity {
    SwipeRefreshLayout main_swipe_refresh;
    RecyclerView main_place_list;
    DeviecAdapter deviecAdapter;
    List<DeviceBean> deviceBeans;
    private int TOAST;
    private String taskId;
    private String type;//type为空：提交设备列表；type不为空：查看设备列表

    @Override
    protected void findViews() {
        main_swipe_refresh = getView(R.id.main_swipe_refresh);
        main_place_list = getView(R.id.main_place_list);
        deviecAdapter = new DeviecAdapter(mContext,type);
        main_place_list.setAdapter(deviecAdapter);
        main_place_list.setLayoutManager(new LinearLayoutManager(mContext));
        main_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TOAST = 1;
                        getData();
                        main_swipe_refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        deviecAdapter.setDeviecInterface(new DeviecAdapter.DeviecInterface() {
            @Override
            public void setJumb(String deviceId) {
                if (type != null && !type.isEmpty()){
                    Bundle bundle = new Bundle();
                    bundle.putString("deviceId", deviceId);
                    bundle.putString("taskId", taskId);
                    jumpTo(DeviceDetailsActivity.class, bundle);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("deviceId", deviceId);
                    jumpTo(DeviceDetailsActivity.class, bundle);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    public void getData() {
        params = new HashMap<>();
        params.put("projectGuid", SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID));
        params.put("taskId", taskId);
        get(BuildConfig.deviceList, GetDeviceListBean.class, false, NetUtils.PATROL);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void formatViews() {
        setTitle(R.string.device_list);
        if (type != null && !type.isEmpty())
            setBaseRightText(R.string.subimt);
    }

    private View noData;

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.deviceList:
                if (baseBean.isSuccessful()) {
                    deviceBeans = ((GetDeviceListBean) baseBean).getData();
                    if (Const.judgeListNull(deviceBeans) != 0) {
                        deviecAdapter.setData(deviceBeans);
                        if (noData != null && noData.getVisibility() == View.VISIBLE) {
                            noData.setVisibility(View.GONE);
                        }
                    } else {
                        deviecAdapter.setData(deviceBeans);
                        noData = noData("", false);
                    }
                } else {
                    ToastUtil.showShortToast(mContext, baseBean.getMessage());
                }
                break;
        }
        if (TOAST != 0) {
            ToastUtil.showShortToast(mContext, baseBean.getMessage());
            TOAST = 0;
        }
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            taskId = bundle.getString("taskId");
            type = bundle.getString("type");
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
    protected int getLayoutId() {
        return R.layout.list_task;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case RIGHT_TEXT_ID:
                if (taskId != null && !taskId.isEmpty()) {
                    boolean b = true;
                    for (DeviceBean bean : deviecAdapter.getData()) {
                        if (!bean.isCheck()){
                            b = bean.isCheck();
                        }
                    }
                    if (b){
                        Bundle bundle = new Bundle();
                        bundle.putString("taskId", taskId);
                        jumpTo(SubmitTaskActivity.class, bundle);
                    }else {
                        ToastUtil.showShortToast(this,"请完成检查后提交");
                    }
                }
                break;
        }
    }
}
