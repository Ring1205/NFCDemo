package com.zxycloud.hzy_xg.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class SubmitTaskActivity extends BaseNetActivity {
    private EditText et_task_describe;
    private String taskId, remark;

    @Override
    protected void findViews() {
        et_task_describe = getView(R.id.et_task_describe);
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.task_submit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                remark = et_task_describe.getText().toString().trim();
                if (!remark.isEmpty()) {
                    params = new HashMap<>();
                    params.put("taskId", taskId);
                    params.put("remark", remark);
                    post(BuildConfig.taskSubmit, BaseBean.class, false, NetUtils.PATROL);
                } else {
                    ToastUtil.showShortToast(this, "清添加描述");
                }
                break;
        }
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.taskSubmit:
                if (baseBean.isSuccessful()) {
                    backTo(MainActivity.class);
                }
                ToastUtil.showShortToast(this, baseBean.getMessage());
                break;
        }
    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle))
            taskId = bundle.getString("taskId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_submit_task;
    }

    @Override
    protected void formatData() {

    }
}
