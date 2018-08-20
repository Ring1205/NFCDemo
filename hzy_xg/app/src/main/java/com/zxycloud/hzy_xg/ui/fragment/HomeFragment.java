package com.zxycloud.hzy_xg.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.fragment.BaseNetFragment;
import com.zxycloud.hzy_xg.bean.GetBean.GetPlanListBean;
import com.zxycloud.hzy_xg.bean.GetBean.GetTaskListBean;
import com.zxycloud.hzy_xg.bean.base.PlanBean;
import com.zxycloud.hzy_xg.bean.base.TaskBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.ui.activity.MainActivity;
import com.zxycloud.hzy_xg.ui.activity.PlanDetailsActivity;
import com.zxycloud.hzy_xg.ui.activity.TaskDetailsActivity;
import com.zxycloud.hzy_xg.ui.adapter.PlanAdapter;
import com.zxycloud.hzy_xg.ui.adapter.TaskAdapter;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog.NO_NET_TAG;

public class HomeFragment extends BaseNetFragment {
    SwipeRefreshLayout main_swipe_refresh;
    RecyclerView main_place_list;
    TaskAdapter taskAdapter;
    List<TaskBean> taskBeans;
    TextView mine_name;
    int TOAST;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void findViews() {
        main_swipe_refresh = getView(R.id.main_swipe_refresh);
        main_place_list = getView(R.id.main_place_list);
        mine_name = getView(R.id.mine_name);
        mine_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MainActivity.mainActivity.button.setVisibility(MainActivity.mainActivity.button.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
                return false;
            }
        });
        taskAdapter = new TaskAdapter(mContext);
        taskAdapter.setType("");
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
                jumpTo(TaskDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        params = new HashMap<>();
        params.put("projectGuid", SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID));
        params.put("pageNumber", 1);
        params.put("pageSize", 100);
        get(BuildConfig.taskLately, GetTaskListBean.class, false, NetUtils.PATROL);
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.app_name, false);
    }

    @Override
    public void onClick(View view) {

    }

    private View noData;
    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.taskLately:
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
                    ToastUtil.showShortToast(mContext, baseBean.getMessage());
                }
                break;
        }
        if (TOAST != 0){
            ToastUtil.showShortToast(mContext,baseBean.getMessage());
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
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }
}
