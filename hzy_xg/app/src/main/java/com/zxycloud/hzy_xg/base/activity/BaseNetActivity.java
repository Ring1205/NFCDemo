package com.zxycloud.hzy_xg.base.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.TxtUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author leiming
 * @date 2018/8/3.
 */

public abstract class BaseNetActivity extends BaseLayoutActivity {
    private NetUtils netUtils;
    protected final String NO_NET_TAG = "no_net";

    /**
     * 所访问尾址列表，用于存储断网时未访问成功的尾址，刷新重新访问
     */
    private List<String> actionList = new ArrayList<>();

    protected Map<String, Object> params;

    private BaseNetActivity baseNetActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netUtils = new NetUtils(mActivity, netRequestCallBack);
        baseNetActivity = this;
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

    public <T extends BaseBean> void postFile(String action, List<File> fileList, Class<T> clazz, String fileType) {
        netUtils.postFile(action, fileList,clazz,fileType);
    }

    private NetUtils.NetRequestCallBack netRequestCallBack = new NetUtils.NetRequestCallBack() {
        @Override
        public void success(String action, BaseBean baseBean, Map tag) {
            actionList.remove(action);
            //noinspection unchecked
            baseNetActivity.success(action, baseBean, tag);
        }

        @Override
        public void error(String action, Throwable e, Map tag) {
            actionList.remove(action);
            //noinspection unchecked
            baseNetActivity.error(action, e.getMessage(), tag);
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
