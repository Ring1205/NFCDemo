package com.zxycloud.hzy_xg.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetUserInfoBean;
import com.zxycloud.hzy_xg.bean.UserAccountBean;
import com.zxycloud.hzy_xg.bean.base.UserBean;
import com.zxycloud.hzy_xg.db.DbUtils;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.MD5;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.TxtUtils;
import com.zxycloud.hzy_xg.widget.BswAlertDialog.BswAlertDialog;
import com.zxycloud.hzy_xg.widget.BswAlertDialog.BswAlertDialogFactory;
import com.zxycloud.hzy_xg.widget.BswAlertDialog.OnDialogClickListener;
import com.zxycloud.hzy_xg.widget.MultifunctionalEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录页面
 *
 * @author leiming
 * @date 2017/10/11
 */
public class LoginActivity extends BaseNetActivity implements OnDialogClickListener, AdapterView.OnItemClickListener, MultifunctionalEditText.OnTextDeletedListener {

    /**
     * loginAccount     ：EditText       , 登录账号输入框
     * loginPassword    ：EditText       , 登录密码输入框
     * rememberPassword  ：CheckBox       , 记住账号
     * showHistoryPhone ：ImageView      , 进入选择历史手机号入口按钮
     * historyPhoneLv   ：ListView       , 展示历史手机号的列表
     * loginLinear      ：LinearLayout   , 展示历史手机号的列表
     */
    private MultifunctionalEditText loginAccount;
    private MultifunctionalEditText loginPassword;
    private CheckBox rememberPassword;
    private ImageView showHistoryPhone;
    private ListView historyPhoneLv;
    private LinearLayout loginLinear;

    private AccountListAdapter listAdapter = new AccountListAdapter();

    /**
     * userName         用户名存储，用于登录验证
     * userPassword     密码存储，用于登录验证
     */
    private String userName = "";
    private String userPassword = "";
    private BswAlertDialog mDialog;

    /**
     * 存储用户信息的数据库
     */
    private DbUtils dbUtils;

    private List<String> accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dbUtils = DbUtils.init(mContext);
                accounts = dbUtils.getAllHistoryAccount(DbUtils.ACCOUNT_TABLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * 登录判断
     */
    private void doLogin() {
        if (TextUtils.isEmpty(SPUtils.getInstance(mContext).getString(SPUtils.USER_TOKEN, ""))) // 推出登录后没有Token，则重新获取
        {
            getToken();
        } else                                                                          // 首次进入APP或者设置了私有云,存在Token,直接进行登录流程
        {
            login();
        }
    }

    /**
     * 登录
     */
    private void login() {
        userName = TxtUtils.getText(loginAccount);
        userPassword = TxtUtils.getText(loginPassword);
        if (TextUtils.isEmpty(userName)) {
            showAlertDialog(R.string.enter_the_account);
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            showAlertDialog(R.string.enter_the_password);
            return;
        }
        params = new HashMap<>();
        params.put("username", userName);
        params.put("password", MD5.getMD5Str(userPassword));
        post(BuildConfig.login, GetUserInfoBean.class, true, NetUtils.SSO);
    }

    /**
     * 展示提示框
     *
     * @param title 提示框信息
     */
    private void showAlertDialog(int title) {
        mDialog = BswAlertDialogFactory.getBswAlertDialog(mActivity, "login_tag", this).onlyMakeSure().setTitle(title).show();
    }

    @Override
    public void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        if (baseBean.isSuccessful()) {
            switch (action) {
                // 根据账号密码登录成功
                case BuildConfig.login:
                    boolean rememberUserAccount = rememberPassword.isChecked();

                    SPUtils.getInstance(mContext).put(SPUtils.PROJECT_ID, "6c9d5c0377df4965869e2fa83c3a2791");

                    // 存储到数据库，用于展示
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DbUtils.USER_ACCOUNT, userName);
                    contentValues.put(DbUtils.USER_PASSWORD, userPassword);
                    contentValues.put(DbUtils.REMEMBER_PW, rememberUserAccount + "");
                    dbUtils.addData(DbUtils.ACCOUNT_TABLE, contentValues, userName);

                    if (rememberUserAccount) {
                        SPUtils.getInstance(mContext).put(SPUtils.USER_PASSWORD, userPassword);
                    } else {
                        SPUtils.getInstance(mContext).remove(SPUtils.USER_PASSWORD);
                    }

                    jumpTo(MainActivity.class);
                    finish();
                    break;

                case BuildConfig.getToken:
                    login();
                    break;
            }
        } else {
            toast(baseBean.getMessage(mContext));
        }
    }

    @Override
    public void error(String action, String errorString, Map tag) {
        toast(errorString);
    }

    @Override
    public void onClick(String tag, View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        loginAccount = getView(R.id.login_account);
        loginPassword = getView(R.id.login_password);
        rememberPassword = getView(R.id.remember_password);
        showHistoryPhone = getView(R.id.show_history_phone);
        historyPhoneLv = getView(R.id.history_phone_lv);
        loginLinear = getView(R.id.login_linear);
    }

    @Override
    protected void formatViews() {
        if (Const.isLogoutTime) {
            Const.isLogoutTime = false;
            showAlertDialog(R.string.account_is_abnormal);
        }
        userName = SPUtils.getInstance(mContext).getString(SPUtils.USER_ACCOUNT, "");
        userPassword = SPUtils.getInstance(mContext).getString(SPUtils.USER_PASSWORD, "");
        rememberPassword.setChecked(SPUtils.getInstance(mContext).getBoolean(SPUtils.USER_PASSWORD_REMEMBER, true));
        if (! TextUtils.isEmpty(userName)) {
            loginAccount.setText(userName);
        }
        if (! TextUtils.isEmpty(userPassword)) {
            loginPassword.setText(userPassword);
        }
        loginAccount.setClear();
        loginAccount.setOnTextDeletedListener(this);
        loginPassword.setPassword();
        historyPhoneLv.setAdapter(listAdapter);
        historyPhoneLv.setOnItemClickListener(this);
        setOnClickListener(R.id.login_set, R.id.login_btn, R.id.login_forget_password, R.id.not_register, R.id.remember_check, R.id.show_history_phone);
    }

    @Override
    protected void formatData() {
//        APP升级
//        startService(new Intent(this, UpdateService.class));

//        无法自动登录显示的提示信息
//        Bundle bundle = getIntent().getBundleExtra("bundle");
//        if (bundle != null) {
//            toast(bundle.getString("message"));
//        }
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 私有云配置
            case R.id.login_set:
                jumpTo(IpResetActivity.class);
                break;

            // 登录
            case R.id.login_btn:
                doLogin();
                break;

            // 忘记密码
//            case R.id.login_forget_password:
//                jumpTo(ForgetPasswordActivity.class);
//                break;

            // 注册
//            case R.id.not_register:
//                jumpTo(RegisterActivity.class);
//                break;

            // 记住密码
            case R.id.remember_check:
                rememberPassword.setChecked(! rememberPassword.isChecked());
                break;

            // 展示历史登录账号
            case R.id.show_history_phone:
                if (showHistoryPhone.getContentDescription().equals(TxtUtils.getText(mContext, R.string.open))) {
                    showHistoryPhone.setContentDescription(TxtUtils.getText(mContext, R.string.close));
                    showHistoryPhone.setImageResource(R.mipmap.up);
                    loginLinear.setVisibility(View.GONE);
                    historyPhoneLv.setVisibility(View.VISIBLE);
                } else {
                    showHistoryPhone.setContentDescription(TxtUtils.getText(mContext, R.string.open));
                    showHistoryPhone.setImageResource(R.mipmap.down);
                    loginLinear.setVisibility(View.VISIBLE);
                    historyPhoneLv.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final UserAccountBean bean = dbUtils.hasAccount(new String[] {accounts.get(position)});
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginAccount.setText(bean.getAccount());
                        boolean isRememberPassword = bean.isRememberPW();
                        rememberPassword.setChecked(isRememberPassword);
                        if (isRememberPassword) {
                            loginPassword.setText(bean.getPassword());
                        } else {
                            loginPassword.setText("");
                        }

                        showHistoryPhone.setContentDescription(TxtUtils.getText(mContext, R.string.open));
                        showHistoryPhone.setImageResource(R.mipmap.down);
                        loginLinear.setVisibility(View.VISIBLE);
                        historyPhoneLv.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDeleted() {
        loginPassword.setText("");
    }

    private class AccountListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Const.judgeListNull(accounts);
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"InflateParams", "RtlHardcoded"})
        @Override
        public View getView(final int position, View currentView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (currentView == null) {
                holder = new ViewHolder();
                currentView = getLayoutInflater().inflate(R.layout.spinner_item, null);
                holder.tv = currentView.findViewById(R.id.spinner_item);
                holder.tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                holder.tv.setBackgroundColor(Color.WHITE);

                holder.iv = currentView.findViewById(R.id.spinner_delete);
                holder.iv.setBackgroundColor(Color.WHITE);
                holder.iv.setVisibility(View.VISIBLE);
                holder.iv.setTag(position);
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dbUtils.delData(DbUtils.ACCOUNT_TABLE, accounts.get((Integer) v.getTag()));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        accounts = dbUtils.getAllHistoryAccount(DbUtils.ACCOUNT_TABLE);
                                        listAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }).start();
                    }
                });

                currentView.setTag(holder);
            } else {
                holder = (ViewHolder) currentView.getTag();
            }
            holder.tv.setText(accounts.get(position));
            return currentView;
        }

        class ViewHolder {
            TextView tv;
            ImageView iv;
        }
    }
}

