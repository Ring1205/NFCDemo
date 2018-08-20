package com.zxycloud.hzy_xg.base.fragment;

import android.os.Bundle;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment基类，用于网络请求
 *
 * @author leiming
 * @date 2017/10/11
 */
public abstract class BaseNetFragment extends BaseFragment {
    private NetUtils netUtils;
    /**
     * 所访问尾址列表，用于存储断网时未访问成功的尾址，刷新重新访问
     */
    private List<String> actionList = new ArrayList<>();

    protected Map<String, Object> params;

    private BaseNetFragment baseNetFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netUtils = new NetUtils(mActivity, netRequestCallBack);
        baseNetFragment = this;
    }

    protected void getToken() {
        if (actionList.contains(BuildConfig.getToken)) {
            return;
        }

        actionList.add(BuildConfig.getToken);
        netUtils.get(BuildConfig.getToken, params, BaseBean.class, new HashMap<String, Object>(), false, NetUtils.SSO);
    }

    protected void getIp(String ip) {
        netUtils.get(ip, params, BaseBean.class, new HashMap<String, Object>(), true, NetUtils.IP_RESET);
    }

    protected <T extends BaseBean> void get(final String action, Class<T> clazz, boolean showDialog, int inputType) {
        get(action, clazz, new HashMap<String, Object>(), showDialog, inputType);
    }

    protected <T extends BaseBean> void get(final String action, Class<T> clazz, Map<String, ?> tag, boolean showDialog, int inputType) {
        netUtils.get(action, params, clazz, tag, showDialog, inputType);
    }

    protected <T extends BaseBean> void post(final String action, Class<T> clazz, boolean showDialog, int inputType) {
        post(action, params, clazz, new HashMap<String, Object>(), showDialog, inputType);
    }

    protected <T extends BaseBean> void post(final String action, Class<T> clazz, Map<String, ?> tag, boolean showDialog, int inputType) {
        netUtils.post(action, params, clazz, tag, showDialog, inputType);
    }

    protected <T extends BaseBean> void post(final String action, Object o, Class<T> clazz, boolean showDialog, int inputType) {
        post(action, o, clazz, new HashMap<String, Object>(), showDialog, inputType);
    }

    protected <T extends BaseBean> void post(final String action, Object o, Class<T> clazz, Map<String, ?> tag, boolean showDialog, int inputType) {
        netUtils.post(action, o, clazz, tag, showDialog, inputType);
    }

    private NetUtils.NetRequestCallBack netRequestCallBack = new NetUtils.NetRequestCallBack() {
        @Override
        public void success(String action, BaseBean baseBean, Map tag) {
            actionList.remove(action);
            //noinspection unchecked
            baseNetFragment.success(action, baseBean, tag);
        }

        @Override
        public void error(String action, Throwable e, Map tag) {
            actionList.remove(action);
            //noinspection unchecked
            baseNetFragment.error(action, e.getMessage(), tag);
        }
    };

    protected void removeCookies() {
        netUtils.removeCookies();
    }

    protected abstract void success(String action, BaseBean baseBean, Map<String, ?> tag);

    /**
     * 访问失败回调抽象方法
     *
     * @param action      网络访问尾址
     * @param errorString 所返回的异常
     * @param tag         当接口复用时，用于区分请求的表识
     */
    protected abstract void error(String action, String errorString, Map<String, ?> tag);
}
