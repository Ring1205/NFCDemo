package com.zxycloud.hzy_xg.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import com.zxycloud.hzy_xg.bean.GetBean.GetAlarmTaskListBean;
import com.zxycloud.hzy_xg.bean.GetBean.GetJPushBean;
import com.zxycloud.hzy_xg.bean.GetBean.GetTaskListBean;
import com.zxycloud.hzy_xg.bean.base.TaskBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.ui.adapter.TaskAdapter;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog.NO_NET_TAG;

public class TaskListActivity extends BaseNetActivity {
    SwipeRefreshLayout main_swipe_refresh;
    RecyclerView main_place_list;
    TaskAdapter taskAdapter;
    List<TaskBean> taskBeans;
    private int TOAST;
    private String markId;
    private static GetJPushBean bean;

    public static Intent makeIntent(Context context, GetJPushBean bean1) {
        bean = bean1;
        return new Intent(context, TaskListActivity.class);
    }

    @Override
    protected void findViews() {
        main_swipe_refresh = getView(R.id.main_swipe_refresh);
        main_place_list = getView(R.id.main_place_list);
        taskAdapter = new TaskAdapter(mContext);
        main_place_list.setAdapter(taskAdapter);
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
        taskAdapter.setTaskInterfacer(new TaskAdapter.TaskInterfacer() {
            @Override
            public void getType(String taskId) {
                Bundle bundle = new Bundle();
                bundle.putString("taskId", taskId);
                if (bean != null)
                    bundle.putString("type", "scan");

                else
                    bundle.putString("type", "device");
                jumpTo(TaskDetailsActivity.class, bundle);
            }
        });
    }

    private void getData() {
        if (markId != null && !markId.isEmpty()) {
            params = new HashMap<>();
            params.put("projectGuid", SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID));
            params.put("markId", markId);
            get(BuildConfig.taskScanList, GetTaskListBean.class, false, NetUtils.PATROL);
        } else if (bean != null) {
            params = new HashMap<>();
            params.put("projectGuid", SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID));
            params.put("planId", bean.getPlanId());
            get(BuildConfig.alarmtaskList, GetAlarmTaskListBean.class, false, NetUtils.PATROL);
        }
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.task_list_title);
        resetBaseBack();
    }

    private View noData;

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.taskScanList:
                if (baseBean.isSuccessful()) {
                    taskBeans = ((GetTaskListBean) baseBean).getData();
                    if (Const.judgeListNull(taskBeans) != 0) {
                        taskAdapter.setData(taskBeans);
                        if (noData != null && noData.getVisibility() == View.VISIBLE) {
                            noData.setVisibility(View.GONE);
                        }
                    } else {
                        taskAdapter.setData(taskBeans);
                        noData = noData("", false);
                    }
                } else {
                    noData = noData("", false);
                    ToastUtil.showShortToast(mContext, baseBean.getMessage());
                }
                break;
            case BuildConfig.alarmtaskList:
                if (baseBean.isSuccessful()) {
                    taskBeans = ((GetAlarmTaskListBean) baseBean).getData();
                    if (Const.judgeListNull(taskBeans) != 0) {
                        taskAdapter.setData(taskBeans);
                        if (noData != null && noData.getVisibility() == View.VISIBLE) {
                            noData.setVisibility(View.GONE);
                        }
                    } else {
                        taskAdapter.setData(taskBeans);
                        noData = noData("", false);
                    }
                } else {
                    noData = noData("", false);
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
            markId = bundle.getString("labelID");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_task;
    }

    @Override
    protected void formatData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case BACK_ID:
                if (hasActivity(MainActivity.class)) {
                    finish();
                } else {
                    jumpTo(MainActivity.class);
                    this.finish();
                }
                break;
        }
    }
}
