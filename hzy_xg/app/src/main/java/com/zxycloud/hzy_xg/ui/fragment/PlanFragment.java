package com.zxycloud.hzy_xg.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.fragment.BaseNetFragment;
import com.zxycloud.hzy_xg.bean.GetBean.GetPlanListBean;
import com.zxycloud.hzy_xg.bean.base.PlanBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.ui.activity.PlanDetailsActivity;
import com.zxycloud.hzy_xg.ui.adapter.PlanAdapter;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog.NO_NET_TAG;

public class PlanFragment extends BaseNetFragment {
    SwipeRefreshLayout main_swipe_refresh;
    RecyclerView main_place_list;
    PlanAdapter planAdapter;
    List<PlanBean> planBeans;
    int TOAST;

    public static PlanFragment newInstance() {
        return new PlanFragment();
    }

    @Override
    protected void findViews() {
        main_swipe_refresh = getView(R.id.main_swipe_refresh);
        main_place_list = getView(R.id.main_place_list);
        planAdapter = new PlanAdapter(mContext);
        main_place_list.setAdapter(planAdapter);
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
        planAdapter.setPlanInterface(new PlanAdapter.PlanInterface() {
            @Override
            public void setPlanId(String planId) {
                Bundle bundle = new Bundle();
                bundle.putString("planId",planId);
                jumpTo(PlanDetailsActivity.class,bundle);
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
        get(BuildConfig.planList, GetPlanListBean.class, false, NetUtils.PATROL);
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
            case BuildConfig.planList:
                if (baseBean.isSuccessful()) {
                    planBeans = ((GetPlanListBean) baseBean).getData();
                    if (Const.judgeListNull(planBeans) != 0) {
                        planAdapter.setData(planBeans);
                        if (noData != null && noData.getVisibility() == View.VISIBLE) {
                            noData.setVisibility(View.GONE);
                        }
                    } else {
                        planAdapter.setData(planBeans);
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
