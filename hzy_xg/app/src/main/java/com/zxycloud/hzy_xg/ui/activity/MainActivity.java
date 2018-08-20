package com.zxycloud.hzy_xg.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.zxycloud.hzy_xg.App;
import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseFragmentActivity;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.service.InitService;
import com.zxycloud.hzy_xg.ui.fragment.HomeFragment;
import com.zxycloud.hzy_xg.ui.fragment.LabelFragment;
import com.zxycloud.hzy_xg.ui.fragment.MineFragment;
import com.zxycloud.hzy_xg.ui.fragment.TaskFragment;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialogFactory;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.OnDialogClickListener;
import com.zxycloud.hzy_xg.widget.PickerScrollView;
import com.zxycloud.hzy_xg.widget.Pickers;
import com.zxycloud.hzy_xg.widget.TabView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseFragmentActivity implements OnDialogClickListener {
    /**
     * 选择框区分标签
     * <p>
     * UPDATE_TAG   版本升级标志
     * FINISH_TAG   退出APP标志
     */
    private final String UPDATE_TAG = "update_tag";
    private final String FINISH_TAG = "finish_tag";
    /**
     * 当前显示Fragment对应TabView的Id
     */
    private int currentId;
    private CustomAlertDialog mDialog;
    private TabView mainHomeTab, mainLabelTab, mainTaskTab, mainMineTab;
    private HomeFragment homeFragment;
    private LabelFragment labelFragment;
    private MineFragment mineFragment;
    private TaskFragment taskFragment;
    private AlertDialog dialog;
    public Button button;
    private String[] name;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        startService(new Intent(this, InitService.class));
        // 初始化下方的展示按钮
        mainHomeTab.setData(R.drawable.tab_home_selector);
        mainLabelTab.setData(R.drawable.tab_label_selector);
        mainTaskTab.setData(R.drawable.tab_record_selector);
        mainMineTab.setData(R.drawable.tab_mine_selector);

        homeFragment = HomeFragment.newInstance();
        labelFragment = LabelFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        taskFragment = TaskFragment.newInstance();

        addFragment(R.id.main_fragment, homeFragment, labelFragment, taskFragment, mineFragment);
        hideFragment(labelFragment, taskFragment, mineFragment);
        showFragment(homeFragment);

        // 默认选中首页页面
        mainHomeTab.setSelected(true);
        if (!getSPUtil().getBoolean(SPUtils.INIT_HOMEPAGE_GUIDE, false)) {
            getSPUtil().put(SPUtils.INIT_HOMEPAGE_GUIDE, true);
        }

        initPicker();

        button = getView(R.id.bt_scan);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog.show();
                Window window = dialog.getWindow();
                Display display = getWindowManager().getDefaultDisplay();
                WindowManager.LayoutParams params = window.getAttributes();
                params.x = (int) (display.getWidth() * 0.60);
                params.y = (int) (display.getHeight() * 0.19);
                window.setAttributes(params);
                window.setLayout((int) (display.getWidth() * 0.4), (int) (display.getHeight() * 0.48));
                return false;
            }
        });

        setPushId();
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {

    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {

    }

    /**
     * 更新极光ID
     */
    public void setPushId() {
        params = new HashMap<>();
        params.put("pushId", App.getInstance().getRegistrationId());
        params.put("serviceProviderId", "1");
        get(BuildConfig.setPushId, BaseBean.class, false, NetUtils.SSO);
    }

    private int pickerId;

    private void initPicker() {
        View outerView = LayoutInflater.from(this).inflate(R.layout.view_picker_scroll, null);
        PickerScrollView pickerscrlllview = outerView.findViewById(R.id.pickerview);
        List<Pickers> list = new ArrayList<>(); // 滚动选择器数据
        name = getResources().getStringArray(R.array.scan_style);
        for (int i = 0; i < name.length; i++) {
            list.add(new Pickers(name[i], i));
        }
        pickerscrlllview.setOnSelectListener(new PickerScrollView.onSelectListener() {
            @Override
            public void onSelect(Pickers pickers) {
                pickerId = pickers.getShowId();
            }
        });
        // 设置数据，默认选择第一条
        pickerscrlllview.setData(list);
        pickerscrlllview.setSelected(0);

        dialog = new AlertDialog.Builder(this)
                .setTitle("icon选择器")
                .setView(outerView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        button.setText(name[pickerId]);
                    }
                })
                .create();

    }

    @Override
    protected void findViews() {
        mainHomeTab = getView(R.id.main_home_tab);
        mainLabelTab = getView(R.id.main_label_tab);
        mainTaskTab = getView(R.id.main_task_tab);
        mainMineTab = getView(R.id.main_mine_tab);
    }

    /**
     * 移除状态（隐藏所有Fragment，并将选中状态置false）
     */
    private void removeState() {
        hideFragment(homeFragment, labelFragment, taskFragment, mineFragment);
        mainHomeTab.setSelected(false);
        mainLabelTab.setSelected(false);
        mainTaskTab.setSelected(false);
        mainMineTab.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSPUtil().getBoolean(SPUtils.FIRE_PUSH, false)) {
            getSPUtil().put(SPUtils.FIRE_PUSH, false);
            if (currentId != R.id.main_home_tab) {
                removeState();
                mainHomeTab.setSelected(true);
                showFragment(homeFragment);
                currentId = R.id.main_home_tab;
            }
        }
    }

    @Override
    protected void getBundle(Bundle bundle) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.bt_scan, R.id.ib_scan, R.id.main_home_tab, R.id.main_label_tab, R.id.main_task_tab, R.id.main_mine_tab);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) // 首页点击返回键，提示用户是否确定退出APP
        {
            showDialog(R.string.log_out, 0, FINISH_TAG);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void formatData() {
    }

    /**
     * 显示选择框
     *
     * @param title   选择框标题
     * @param message 选择框信息
     * @param tag     选择框区分标签
     */
    private void showDialog(int title, int message, String tag) {
        switch (tag) {
            // 是否更新APP选择框
            case UPDATE_TAG:
                mDialog = CustomAlertDialogFactory.getCustomAlertDialog(mActivity, tag, this).setTitle(title).touchOutside().setContent(message).show();
                break;

            // 首页点击返回键是否退出APP选择框
            case FINISH_TAG:
                mDialog = CustomAlertDialogFactory.getCustomAlertDialog(mActivity, tag, this).setTitle(title).show();
                break;
        }
    }

    @Override
    public void onClick(String tag, View view) {
        switch (view.getId()) {
            // 点击选择框确定键
            case CustomAlertDialog.CONFIRM_ID:
                switch (tag) {
                    // 退出APP
                    case FINISH_TAG:
                        finish();
                        break;

                    // 更新APP
                    case UPDATE_TAG:
                        break;
                }
                break;

            // 点击选择框确定键
            case CustomAlertDialog.CANCEL_ID:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int clickId = view.getId();
        switch (clickId) {
            // 首页Fragment点击
            case R.id.main_home_tab:
                if (currentId != clickId) {
                    removeState();
                    mainHomeTab.setSelected(true);
                    showFragment(homeFragment);
                    currentId = clickId;
                }
                break;

            // 标签Fragment点击
            case R.id.main_label_tab:
                if (currentId != clickId) {
                    removeState();
                    mainLabelTab.setSelected(true);
                    showFragment(labelFragment);
                    currentId = clickId;
                }
                break;

            // 任务Fragment点击
            case R.id.main_task_tab:
                if (currentId != clickId) {
                    removeState();
                    mainTaskTab.setSelected(true);
                    showFragment(taskFragment);
                    currentId = clickId;
                }
                break;

            // 我的Fragment点击
            case R.id.main_mine_tab:
                if (currentId != clickId) {
                    removeState();
                    mainMineTab.setSelected(true);
                    showFragment(mineFragment);
                    currentId = clickId;
                }
                break;

            case R.id.ib_scan:
            case R.id.bt_scan:
                Bundle bundle = new Bundle();
                bundle.putString("jump", "scan");
                jumpTo(ScanActivity.class, bundle);
                break;
        }
    }
}
