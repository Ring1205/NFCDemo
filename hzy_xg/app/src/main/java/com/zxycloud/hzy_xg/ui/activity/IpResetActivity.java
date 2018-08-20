package com.zxycloud.hzy_xg.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetIpBean;
import com.zxycloud.hzy_xg.bean.base.IpBean;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.TxtUtils;
import com.zxycloud.hzy_xg.widget.BswAlertDialog.BswAlertDialog;
import com.zxycloud.hzy_xg.widget.BswAlertDialog.BswAlertDialogFactory;
import com.zxycloud.hzy_xg.widget.BswAlertDialog.OnDialogClickListener;
import com.zxycloud.hzy_xg.widget.MultifunctionalEditText;
import com.zxycloud.hzy_xg.widget.MyCheckBox;

import java.util.Map;

/**
 * 私有云设置页面
 *
 * @author leiming
 * @date 2017/10/11
 */
public class IpResetActivity extends BaseNetActivity implements OnDialogClickListener {

    /**
     * ipAddressEt          ：EditText       , IP地址输入框
     * ipPortEt             ：EditText       , 端口输入框
     * ipPortUserType       ：TextView       , 用户类型显示框：用户登录/调试登录
     * ipPortUserTypeShow   ：LinearLayout   , 切换模式展示
     * ipHttps              ：MyCheckBox     , 是否进行Https加密选择框
     */
    private MultifunctionalEditText ipAddressEt;
    private MultifunctionalEditText ipPortEt;
    private MyCheckBox ipHttps;
    private BswAlertDialog mDialog;
    private String url;
    private String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题
        hideTitle();

        // 断网关闭当前页面
        if (! isNetworkAvailable()) {
            mDialog = BswAlertDialogFactory.getBswAlertDialog(mActivity, "", new OnDialogClickListener() {
                @Override
                public void onClick(String tag, View view) {
                    finish();
                }
            }).setTitle(R.string.network_is_unavailable).onlyMakeSure().touchOutside().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    finish();
                    return true;
                }
            }).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ip_reset;
    }

    @Override
    protected void findViews() {
        ipAddressEt = getView(R.id.ip_address_et);
        ipPortEt = getView(R.id.ip_port_et);
        ipHttps = getView(R.id.ip_https);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void formatViews() {

        ipAddressEt.setClear();
        ipPortEt.setClear();

        // 若当前存在共有网关/私有网关地址的时候继续执行回显，若为空则不进行回显
        String url = SPUtils.getInstance(mContext).getString(SPUtils.IP_URL);
        String urlTemp;
        if (TextUtils.isEmpty(url)) {
            urlTemp = SPUtils.getInstance(mContext).getString(SPUtils.SSO, BuildConfig.ssoUrl);
            if (TextUtils.isEmpty(urlTemp)) {
                return;
            }
            ipHttps.setChecked(urlTemp.contains("https://"));
            urlTemp = urlTemp.replace("https://", "").replace("http://", "");
            String[] temp = urlTemp.split(":");
            if (temp.length >= 2) {
                ipAddressEt.setText(temp[0]);
                ipPortEt.setText(temp[1].substring(0, temp[1].indexOf("/")));
            } else {
                ipAddressEt.setText(temp[0].substring(0, temp[0].indexOf("/")));
                ipPortEt.setText("80");
            }
        } else {
            ipAddressEt.setText(url);
            String port = SPUtils.getInstance(mContext).getString(SPUtils.IP_PORT);
            if (! TextUtils.isEmpty(port)) {
                ipPortEt.setText(port);
            }
            ipHttps.setChecked(SPUtils.getInstance(mContext).getBoolean(SPUtils.IP_HTTPS));
        }
        setOnClickListener(R.id.make_sure_btn, R.id.make_cancel, R.id.ip_https_layout);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    /**
     * 提示框
     *
     * @param title 提示信息
     */
    private void showaAlertDialog(int title) {
        mDialog = BswAlertDialogFactory.getBswAlertDialog(mActivity, "ip_tag", this).onlyMakeSure().setTitle(title).show();
    }

    @Override
    public void onClick(String tag, View view) {

    }

    @Override
    public void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        IpBean ipBean = ((GetIpBean) baseBean).getApp();
        if (ipBean == null || ipBean.cannotUse()) {
            toast(R.string.failed_to_set_up_the_server);
            return;
        }
        SPUtils.getInstance(mContext).put(SPUtils.SSO, ipBean.getSsoUrl()).put(SPUtils.LOG, ipBean.getApplogUrl()).put(SPUtils.UPLOAD, ipBean.getUploadUrl()).put(SPUtils.PATROL, ipBean.getPatrolUrl()).put(SPUtils.PERMISSION, ipBean.getPermissionUrl());
        removeCookies();
    }

    @Override
    public void error(String action, String errorMessage, Map<String, ?> tag) {
        showaAlertDialog(R.string.server_address_error);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 确认设置私有云
            case R.id.make_sure_btn:
                url = TxtUtils.getText(ipAddressEt);
                port = TxtUtils.getText(ipPortEt);
                if (TextUtils.isEmpty(url)) {
                    showaAlertDialog(R.string.enter_the_domain_name);
                    return;
                }
                String ip = url;
                if (ip.contains("：")) {
                    ip = ip.replace("：", ":");
                } else if (! ip.contains(":")) {
                    if (! TextUtils.isEmpty(port)) {
                        if (port.contains("：")) {
                            ip += port.replace("：", ":");
                        } else if (! port.contains(":")) {
                            ip += String.format(":%s", port);
                        }
                    } else {
                        ip += ipHttps.isChecked() ? ":443" : ":80";
                    }
                }
                if (ipHttps.isChecked()) {
                    ip = String.format("https://%s/%s", ip, BuildConfig.ipActionSSL);
                } else {
                    ip = String.format("http://%s/%s", ip, BuildConfig.ipAction);
                }
                getIp(ip);
                break;

            // 放弃设置公有云
            case R.id.make_cancel:
                jumpTo(LoginActivity.class);
                break;

            // 更新Https加密状态
            case R.id.ip_https_layout:
                ipHttps.hasClicked();
                break;
        }
    }
}
