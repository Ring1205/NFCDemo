package com.zxycloud.hzy_xg.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetTaskDetailBean;
import com.zxycloud.hzy_xg.bean.base.TaskDetailBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.GlideUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;
import com.zxycloud.hzy_xg.utils.TxtUtils;

import java.util.HashMap;
import java.util.Map;

import static com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog.NO_NET_TAG;

public class TaskDetailsActivity extends BaseNetActivity {
    TextView tv_label_address, tv_label_name, tv_label_num, tv_task_detalis, tv_task_routing, tv_task_incharge, tv_task_state, tv_task_time, tv_task_name;
    ImageView iv_device;
    Button bt_task;
    TaskDetailBean taskBean;
    private String taskId, type;

    @Override
    protected void findViews() {
        tv_label_address = getView(R.id.tv_label_address);
        tv_label_name = getView(R.id.tv_label_name);
        tv_label_num = getView(R.id.tv_label_num);
        tv_task_detalis = getView(R.id.tv_task_detalis);
        tv_task_routing = getView(R.id.tv_task_routing);
        tv_task_incharge = getView(R.id.tv_task_incharge);
        tv_task_state = getView(R.id.tv_task_state);
        tv_task_time = getView(R.id.tv_task_time);
        tv_task_name = getView(R.id.tv_task_name);
        iv_device = getView(R.id.iv_device);
        bt_task = getView(R.id.bt_task);
        if ("device".equals(type)){
            bt_task.setText(R.string.scan_task);
        }else if ("scan".equals(type)){
            bt_task.setText(R.string.scan_nfc);
        }else {
            bt_task.setText(R.string.scan_task_list);
        }
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.taskdetail:
                if (baseBean.isSuccessful()) {
                    taskBean = ((GetTaskDetailBean) baseBean).getData();
                    tv_task_name.setText("任务名称：".concat(TxtUtils.getText(taskBean.getTaskName())));
                    tv_task_time.setText("任务时间：".concat(TxtUtils.getText(taskBean.getTaskTime())));
                    tv_task_state.setText("任务状态：".concat(TxtUtils.getText(taskBean.getTaskState())));
                    tv_task_incharge.setText("责任主管：".concat(TxtUtils.getText(taskBean.getInchargePerson())));
                    tv_task_routing.setText("巡检人员：".concat(TxtUtils.getText(taskBean.getRoutingPerson())));
                    tv_task_detalis.setText("任务描述：".concat(TxtUtils.getText(taskBean.getTaskDescription())));
                    tv_label_num.setText("标签编号：".concat(TxtUtils.getText(taskBean.getMarkNumber())));
                    tv_label_name.setText("标签名称：".concat(TxtUtils.getText(taskBean.getMarkName())));
                    tv_label_address.setText("标签安装位置：".concat(TxtUtils.getText(taskBean.getMarkInstallPos())));
                    if (taskBean.getMarkInstallPosImgUrl() != null && !taskBean.getMarkInstallPosImgUrl().isEmpty()){
                        GlideUtils.loadImageView(this,taskBean.getMarkInstallPosImgUrl(),iv_device);
                    }
                } else {
                    ToastUtil.showShortToast(mContext, baseBean.getMessage());
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
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            taskId = bundle.getString("taskId");
            type = bundle.getString("type");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_details;
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.task_details);
        setOnClickListener(R.id.bt_task);
    }

    @Override
    protected void onResume() {
        super.onResume();
        params = new HashMap<>();
        params.put("taskId", taskId);
        get(BuildConfig.taskdetail, GetTaskDetailBean.class, false, NetUtils.PATROL);
    }

    @Override
    protected void formatData() {}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_task:
                if ("device".equals(type)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", taskId);
                    bundle.putString("type", "device");
                    jumpTo(DeviceListActivity.class, bundle);
                } else if ("scan".equals(type)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("jump", TxtUtils.getText(taskBean.getMarkNumber()));
                    bundle.putString("taskId", taskId);
                    jumpTo(ScanActivity.class, bundle);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", taskId);
                    jumpTo(DeviceListActivity.class, bundle);
                }
                break;
        }
    }
}
