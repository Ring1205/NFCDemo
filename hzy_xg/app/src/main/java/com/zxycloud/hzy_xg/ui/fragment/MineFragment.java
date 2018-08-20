package com.zxycloud.hzy_xg.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseActivity;
import com.zxycloud.hzy_xg.base.fragment.BaseNetFragment;
import com.zxycloud.hzy_xg.bean.GetBean.GetUserInfoBean;
import com.zxycloud.hzy_xg.bean.base.UserBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.ui.activity.LoginActivity;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialog;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.CustomAlertDialogFactory;
import com.zxycloud.hzy_xg.widget.CustomAlertDialog.OnDialogClickListener;

import java.util.Map;

public class MineFragment extends BaseNetFragment implements OnDialogClickListener {
    /**
     * 登出标签
     */
    private final String LOGOUT_TAG = "logout_tag";
    private CustomAlertDialog mDialog;
    private NetUtils netUtils;

    private TextView mine_name,mine_telephone;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //请求用户信息
//        netUtils = new NetUtils(mActivity, netRequestCallBack);
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {

    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void findViews() {
        mine_name = getView(R.id.mine_name);
        mine_telephone = getView(R.id.mine_telephone);
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.mine_title, false);
        mine_name.setText(SPUtils.getInstance(mContext).getString(SPUtils.USER_PHONE));
        mine_telephone.setText(SPUtils.getInstance(mContext).getString(SPUtils.USER_PHONE));
        setOnClickListener(R.id.mine_logout);
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
            case R.id.mine_logout:
                showDialog(R.string.log_out_current_account, LOGOUT_TAG);
                break;
        }
    }

    private void showDialog(int title, String tag) {
        mDialog = CustomAlertDialogFactory.getCustomAlertDialog(mActivity, tag, this).setTitle(title).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onClick(String tag, View view) {
        if (tag.equals(LOGOUT_TAG)) {
            if (view.getId() == CustomAlertDialog.CONFIRM_ID) {
                post(BuildConfig.logout, BaseBean.class, false, NetUtils.SSO);
                if (!BaseActivity.hasActivity(LoginActivity.class)) {
                    jumpTo(LoginActivity.class);
                }
                ((BaseActivity) mActivity).finishActivity();
            }
        }
    }
    private NetUtils.NetRequestCallBack netRequestCallBack = new NetUtils.NetRequestCallBack() {
        @Override
        public void success(String action, BaseBean baseBean, Map tag) {
            if (baseBean.isSuccessful()) {
                switch (action) {
                    case BuildConfig.getUserInfo:
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
