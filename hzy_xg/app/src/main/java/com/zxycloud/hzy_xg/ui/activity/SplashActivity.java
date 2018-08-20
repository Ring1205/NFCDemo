package com.zxycloud.hzy_xg.ui.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseActivity;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetUserInfoBean;
import com.zxycloud.hzy_xg.bean.base.UserBean;
import com.zxycloud.hzy_xg.db.DbUtils;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.PermissionUtils;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.TimerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动页
 *
 * @author leiming
 * @date 2018/4/25 16:03
 */
public class SplashActivity extends BaseActivity {

    private NetUtils netUtils;
    private boolean getUserInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        netUtils = new NetUtils(mActivity, netRequestCallBack);
        if (SPUtils.getInstance(mContext).getBoolean(SPUtils.INIT_PERMISSION)) {
            loginJudge();
            if (netUtils.hasToken()) {
                netUtils.get(BuildConfig.getUserInfo, new HashMap<String, Object>(), GetUserInfoBean.class, new HashMap<String, Object>(), false, NetUtils.SSO);
            }
        } else {
            PermissionUtils.setRequestPermissions(mActivity, new PermissionUtils.PermissionGrant() {
                @Override
                public Integer[] onPermissionGranted() {
                    return new Integer[] {PermissionUtils.CODE_ALL_PERMISSION};
                }

                @Override
                public void onRequestResult(List<String> deniedPermission) {
                    SPUtils.getInstance(mContext).put(SPUtils.INIT_PERMISSION, true);
                    jumpTo(LoginActivity.class);
                    finish();
                }
            });
        }
    }

    private void loginJudge() {
        TimerUtils timerUtils = new TimerUtils(1800, 1800, onBaseTimerCallBack);
        timerUtils.start();
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private TimerUtils.OnBaseTimerCallBack onBaseTimerCallBack = new TimerUtils.OnBaseTimerCallBack() {

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            jumpTo(getUserInfo ? MainActivity.class : LoginActivity.class);
            finish();
        }
    };

    private NetUtils.NetRequestCallBack netRequestCallBack = new NetUtils.NetRequestCallBack() {
        @Override
        public void success(String action, BaseBean baseBean, Map tag) {
            if (baseBean.isSuccessful()) {
                switch (action) {
                    case BuildConfig.getUserInfo:
                        getUserInfo = true;
                        UserBean userBean = ((GetUserInfoBean) baseBean).getData();
                        String nickname = userBean.getNickName();
                        // 用户账号
                        SPUtils.getInstance(mContext).put(SPUtils.USER_NAME, nickname);
                        // 用户手机号
                        SPUtils.getInstance(mContext).put(SPUtils.USER_PHONE, userBean.getPhone());
                        // 存储用户Id
                        SPUtils.getInstance(mContext).put(SPUtils.USER_ID, userBean.getUserId());
                        break;
                }
            }
        }

        @Override
        public void error(String action, Throwable e, Map tag) {

        }
    };
}
