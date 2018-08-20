package com.zxycloud.hzy_xg.ui.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetPlanDetailBean;
import com.zxycloud.hzy_xg.bean.base.PlanDetailBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.TxtUtils;

import java.util.HashMap;
import java.util.Map;

public class PlanDetailsActivity extends BaseNetActivity {
    TextView tv_plan_map, tv_task_time, tv_task_remind, tv_task_number, tv_task_state, tv_plan_describe, tv_projectid, tv_create_time, tv_plan_name;
    private String planId;

    @Override
    protected void findViews() {
        tv_plan_map = getView(R.id.tv_plan_map);
        tv_task_time = getView(R.id.tv_task_time);
        tv_task_remind = getView(R.id.tv_task_remind);
        tv_task_number = getView(R.id.tv_task_number);
        tv_task_state = getView(R.id.tv_task_state);
        tv_plan_describe = getView(R.id.tv_plan_describe);
        tv_projectid = getView(R.id.tv_projectid);
        tv_create_time = getView(R.id.tv_create_time);
        tv_plan_name = getView(R.id.tv_plan_name);
    }

    PlanDetailBean planDetailBean;
    private View noData;
    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.planDetail:
                if (baseBean.isSuccessful()) {
                    planDetailBean = ((GetPlanDetailBean) baseBean).getData();
                    if (planDetailBean != null) {
                        if (noData != null && noData.getVisibility() == View.VISIBLE) {
                            noData.setVisibility(View.GONE);
                        }
                        String marks = "";
                        for (String s : planDetailBean.getMarkNames()) {
                            if (!marks.isEmpty()) {
                                marks = marks.concat(",").concat(s);
                            } else {
                                marks = s;
                            }
                        }
                        tv_plan_map.setText("标签集合：\n\u3000\u3000".concat(marks));
                        tv_create_time.setText("创建时间：".concat(TxtUtils.getText(planDetailBean.getCreateTime())));
                        tv_plan_describe.setText("计划描述：".concat(TxtUtils.getText(planDetailBean.getDescribe())));
                        tv_task_time.setText("执行时间：".concat(TxtUtils.getText(planDetailBean.getItemMonthTime())));
                        tv_task_remind.setText("提醒时间：".concat(TxtUtils.getText(planDetailBean.getItemHourTime())));
                        tv_projectid.setText("项目编号：".concat(TxtUtils.getText(planDetailBean.getProjectGuid())));
                        tv_plan_name.setText("计划标题：".concat(TxtUtils.getText(planDetailBean.getTitle())));
                        SpannableStringBuilder builder = new SpannableStringBuilder(tv_plan_map.getText().toString());
                        SpannableStringBuilder builder1 = new SpannableStringBuilder(tv_plan_describe.getText().toString());
                        TxtUtils.setTextColorForeground(tv_plan_map,getResources().getColor(R.color.text_gray_color),5,tv_plan_map.getText().toString().length());
                        TxtUtils.setTextColorForeground(tv_plan_describe,getResources().getColor(R.color.text_gray_color),5,tv_plan_describe.getText().toString().length());
                        switch (planDetailBean.getGenTaskType()) {
                            case 1:
                                tv_task_state.setText("任务周期：一天");
                                tv_task_number.setText("循环时间：".concat(TxtUtils.getText(planDetailBean.getGenTaskCycle())).concat("天"));
                                break;
                            case 2:
                                tv_task_state.setText("任务周期：一周");
                                tv_task_number.setText("循环时间：".concat(TxtUtils.getText(planDetailBean.getGenTaskCycle())).concat("周"));
                                break;
                            case 3:
                                tv_task_state.setText("任务周期：一个月");
                                tv_task_number.setText("循环时间：".concat(TxtUtils.getText(planDetailBean.getGenTaskCycle())).concat("个月"));
                                break;
                            case 4:
                                tv_task_state.setText("任务周期：一个季度");
                                tv_task_number.setText("循环时间：".concat(TxtUtils.getText(planDetailBean.getGenTaskCycle())).concat("个季度"));
                                break;
                            case 5:
                                tv_task_state.setText("任务周期：一年");
                                tv_task_number.setText("循环时间：".concat(TxtUtils.getText(planDetailBean.getGenTaskCycle())).concat("年"));
                                break;
                        }
                    }else {
                        noData = noData("", false);
                    }
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
    protected void onResume() {
        super.onResume();
        params = new HashMap<>();
        params.put("planId", planId);
        get(BuildConfig.planDetail, GetPlanDetailBean.class, false, NetUtils.PATROL);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plan_details;
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.home_title);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            planId = bundle.getString("planId");
        }
    }

    @Override
    public void onClick(View view) {

    }
}
