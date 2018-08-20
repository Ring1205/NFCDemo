package com.zxycloud.hzy_xg.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.fragment.BaseNetFragment;
import com.zxycloud.hzy_xg.bean.GetBean.GetLabelBean;
import com.zxycloud.hzy_xg.bean.PostBean.PostDeleteLabelBean;
import com.zxycloud.hzy_xg.bean.base.LabelBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.ui.activity.AddLabelActivity;
import com.zxycloud.hzy_xg.ui.activity.LabelDetailsActivity;
import com.zxycloud.hzy_xg.ui.activity.MainActivity;
import com.zxycloud.hzy_xg.ui.activity.ScanActivity;
import com.zxycloud.hzy_xg.ui.adapter.LabelAdapter;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.JumpToUtils;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialogFactory;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.OnDialogClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog.NO_NET_TAG;

public class LabelFragment extends BaseNetFragment {
    SwipeRefreshLayout main_swipe_refresh;
    RecyclerView main_place_list;
    LabelAdapter labelAdapter;
    int TOAST;

    public static LabelFragment newInstance() {
        return new LabelFragment();
    }

    @Override
    protected void findViews() {
        main_swipe_refresh = getView(R.id.main_swipe_refresh);
        main_place_list = getView(R.id.main_place_list);
        labelAdapter = new LabelAdapter(mContext);
        main_place_list.setLayoutManager(new LinearLayoutManager(mContext));
        main_place_list.setAdapter(labelAdapter);
        labelAdapter.setDeleteabelInterface(new LabelAdapter.DeleteabelInterface() {
            @Override
            public void delete(final int position) {
                CustomAlertDialogFactory.getCustomAlertDialog(mActivity, "", new OnDialogClickListener() {
                    @Override
                    public void onClick(String tag, View view) {
                        if (view.getId() == CustomAlertDialog.CONFIRM_ID) {
                            String labelID = labelBeans.get(position).getId();
                            List<String> list = new ArrayList<>();
                            list.add(labelID);
                            post(BuildConfig.deleteLabel, new PostDeleteLabelBean(list), BaseBean.class, true, NetUtils.PATROL);
                        }
                    }
                }).setTitle(R.string.make_sure_delete_label).show();
            }

            @Override
            public void jumb(String markId) {
                Bundle bundle = new Bundle();
                bundle.putString("markId",markId);
                jumpTo(LabelDetailsActivity.class,bundle);
            }
        });
        main_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        TOAST = 1;
                        main_swipe_refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.label_title, false);
        setBaseRightText(R.string.no_data);
    }

    List<LabelBean> labelBeans;
    private View noData;

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.labelList:
                if (baseBean.isSuccessful()) {
                    labelBeans = ((GetLabelBean) baseBean).getData();
                    if (Const.judgeListNull(labelBeans) != 0) {
                        labelAdapter.getData(labelBeans);
                        if (noData != null && noData.getVisibility() == View.VISIBLE) {
                            noData.setVisibility(View.GONE);
                        }
                    } else {
                        labelAdapter.getData(labelBeans);
                        noData = noData("", false);
                    }
                } else {
                    ToastUtil.showShortToast(mContext, baseBean.getMessage());
                }
                break;
            case BuildConfig.deleteLabel:
                getData();
                break;
        }
        if (TOAST != 0){
            ToastUtil.showShortToast(mContext,baseBean.getMessage());
            TOAST = 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        params = new HashMap<>();
        params.put("pageSize", String.valueOf(100));
        params.put("pageNumber", String.valueOf(1));
        params.put("projectGuid", SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID));
        params.put("rid", "");
        params.put("name", "");
        get(BuildConfig.labelList, GetLabelBean.class, false, NetUtils.PATROL);
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
        return R.layout.fragment_lable;
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case RIGHT_TEXT_ID:
                Bundle bundle = new Bundle();
                bundle.putString("jump", "add");
                jumpTo(ScanActivity.class, bundle);
                break;
        }
    }
}
