package com.zxycloud.hzy_xg.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.fragment.BaseNetFragment;
import com.zxycloud.hzy_xg.ui.activity.MainActivity;
import com.zxycloud.hzy_xg.ui.adapter.CustomerPagerAdapter;

import java.util.Map;

public class TaskFragment extends BaseNetFragment {
    public static final String TASK_IN = "task_in";
    public static final String TASK_OFF = "task_off";
    private String type;

    private CustomerPagerAdapter pagerAdapter;
    private TabLayout linkmanTypeTab;
    private ViewPager linkmanTypePager;
    private TaskListFragment inTaskListFragment, offTaskListFragment;
    public static TaskFragment newInstance() {
        return new TaskFragment();
    }
    @Override
    protected void findViews() {
        linkmanTypeTab = getView(R.id.linkman_type_tab);
        linkmanTypePager = getView(R.id.linkman_type_pager);
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.task_title,false);
        pagerAdapter = new CustomerPagerAdapter(MainActivity.mainActivity.getSupportFragmentManager());
        pagerAdapter.setTitles("进行中", "已完成");

        inTaskListFragment = TaskListFragment.newInstance("2");
        offTaskListFragment = TaskListFragment.newInstance("3");
        pagerAdapter.setFragments(inTaskListFragment, offTaskListFragment);

        linkmanTypePager.setAdapter(pagerAdapter);
        linkmanTypeTab.setupWithViewPager(linkmanTypePager);
        linkmanTypeTab.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {

    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {

    }
}
