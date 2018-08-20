package com.zxycloud.hzy_xg.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetDeviceDetailsBean;
import com.zxycloud.hzy_xg.bean.base.DeviceBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;
import com.zxycloud.hzy_xg.utils.TxtUtils;

import java.util.HashMap;
import java.util.Map;

import static com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog.NO_NET_TAG;

public class DeviceDetailsActivity extends BaseNetActivity {
    TextView tv_device_name, tv_device_number, tv_device_type, tv_device_state, tv_device_address, tv_device_adapter, tv_installlocation;
    LinearLayout ll_type;
    private String deviceId;
    private String taskId;//taskId不为空：提交设备状态；taskId为空：查看设备状态
    private DeviceBean deviceBean;

    @Override
    protected void findViews() {
        tv_device_name = getView(R.id.tv_device_name);
        tv_device_number = getView(R.id.tv_device_number);
        tv_device_type = getView(R.id.tv_device_type);
        tv_device_state = getView(R.id.tv_device_state);
        tv_device_address = getView(R.id.tv_device_address);
        tv_installlocation = getView(R.id.tv_installlocation);
        tv_device_adapter = getView(R.id.tv_device_adapter);
        ll_type = getView(R.id.ll_type);
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.deviceDetails:
                if (baseBean.isSuccessful()) {
                    deviceBean = ((GetDeviceDetailsBean) baseBean).getData();
                    tv_device_name.setText("设备名称：".concat(TxtUtils.getText(deviceBean.getDeviceName())));
                    tv_device_number.setText("设备编号：".concat(TxtUtils.getText(deviceBean.getDeviceNumber())));
                    tv_device_type.setText("设备类型：".concat(TxtUtils.getText(deviceBean.getDeviceType())));
                    tv_device_state.setText("设备状态：".concat(TxtUtils.getText(deviceBean.getDeviceStateName())));
                    tv_device_address.setVisibility(View.GONE);
                    tv_device_address.setText("位置详情：".concat(TxtUtils.getText(deviceBean.getDeviceAddress())));
                    tv_installlocation.setText("安装位置：".concat(TxtUtils.getText(deviceBean.getInstalllocation())));
                    tv_device_adapter.setText("网络适配器：".concat(TxtUtils.getText(deviceBean.getAdapterName())));
                } else {
                    ToastUtil.showShortToast(this, baseBean.getMessage());
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        params = new HashMap<>();
        params.put("deviceId", deviceId);
        get(BuildConfig.deviceDetails, GetDeviceDetailsBean.class, false, NetUtils.PATROL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_declare:
                Bundle bundle = new Bundle();
                bundle.putString("projectGuid", SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID));
                bundle.putString("taskId", taskId);
                bundle.putString("deviceId", deviceId);
                jumpTo(SubmitDeviecExceActivity.class,bundle);
                break;
            case R.id.bt_ok:
                finish();
                break;
        }
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.device_details);
        if (taskId != null && !taskId.isEmpty()){
            hideBack();
        }else {
            ll_type.setVisibility(View.INVISIBLE);
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
        return R.layout.activity_device_details;
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            deviceId = bundle.getString("deviceId");
            taskId = bundle.getString("taskId");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (taskId != null && !taskId.isEmpty()){
            return false;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }
}
