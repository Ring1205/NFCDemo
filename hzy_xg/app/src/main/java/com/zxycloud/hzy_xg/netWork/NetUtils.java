package com.zxycloud.hzy_xg.netWork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zxycloud.hzy_xg.App;
import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;
import com.zxycloud.hzy_xg.utils.TxtUtils;
import com.zxycloud.hzy_xg.widget.BswAlertDialog.BswAlertDialog;
import com.zxycloud.hzy_xg.widget.BswProgressDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetUtils {
    /**
     * SSO
     */
    public final static int SSO = 0x05;
    /**
     * 权限
     */
    public final static int PERMISSION = 0x06;
    /**
     * 巡更
     */
    public final static int PATROL = 0x07;
    /**
     * 文件上传
     */
    public static final int UPLOAD = 0x08;
    /**
     * 错误日志上传
     */
    public static final int LOG = 0x09;
    /**
     * IP重置
     */
    public static final int IP_RESET = 0x0a;

    /**
     * 上下文
     */
    private final Context mContext;
    /**
     * 网络请求结果回调
     */
    private final NetRequestCallBack netRequestCallBack;
    /**
     * header
     */
    private Map<String, String> headerParams;
    /**
     * 加载提示框
     */
    private BswProgressDialog bswProgressDialog;

    /**
     * header cookie
     */
    private InDiskCookieStore cookieStore;

    public NetUtils(Context mContext, NetRequestCallBack netRequestCallBack) {
        this.netRequestCallBack = netRequestCallBack;
        this.mContext = mContext;
        initHeader();
        cookieStore = new InDiskCookieStore(mContext);
        bswProgressDialog = new BswProgressDialog(mContext, R.style.progress_dialog_loading, R.string.is_loading);
    }

    /**
     * 初始化请求头，具体情况根据需求设置
     */
    private void initHeader() {
        if (headerParams == null) {
            headerParams = new HashMap<>();
            headerParams.put("DEVICE_ID", App.getInstance().getAndroidId());
            headerParams.put("Content-Type", "application/json");
            headerParams.put("APP_TYPE", "6");
        }
    }
    /**
     * 初始化数据
     *
     * @param action    当前请求的尾址
     * @param inputType 网络请求类型
     */
    private Retrofit initBaseData(final String action, int inputType) {

        // https信任管理
        TrustManager[] trustManager = new TrustManager[] {
                new X509TrustManager() {

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[0];
                    }
                }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 请求超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        // 请求参数获取
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@android.support.annotation.NonNull Chain chain) throws IOException {
                Request request = chain.request();
                Logger.i("zzz", String.format("request====%s", request.headers().toString()));
                Logger.i("zzz", String.format("request====%s", request.toString()));
                okhttp3.Response proceed = chain.proceed(request);
                Logger.i("zzz", String.format("proceed====%s", proceed.headers().toString()));
                return proceed;
            }
        });
        CookieManager cookieManager = new CookieManager(new InDiskCookieStore(mContext), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        try {
            // https信任
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // 全部信任
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        // 构建Builder，请求结果RxJava接收，使用GSON转化为Bean，
        Retrofit.Builder builder1 = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        switch (inputType) {
            case SSO:
                builder1.baseUrl(String.format("%s%s", SPUtils.getInstance(mContext).getString(SPUtils.SSO, BuildConfig.ssoUrl), action));
                break;

            case PERMISSION:
                builder1.baseUrl(String.format("%s%s", SPUtils.getInstance(mContext).getString(SPUtils.PERMISSION, BuildConfig.permissionUrl), action));
                break;

            case PATROL:
                builder1.baseUrl(String.format("%s%s", SPUtils.getInstance(mContext).getString(SPUtils.PATROL, BuildConfig.patrolUrl), action));
                break;

            case UPLOAD:
                builder1.baseUrl(String.format("%s%s", SPUtils.getInstance(mContext).getString(SPUtils.UPLOAD, BuildConfig.fileUrl), action));
                break;

            case LOG:
                builder1.baseUrl(String.format("%s%s", SPUtils.getInstance(mContext).getString(SPUtils.LOG, BuildConfig.logUrl), action));
                break;

            case IP_RESET:
                builder1.baseUrl(action);
                break;
        }

        return builder1.build();
    }

    /**
     * Get请求
     *
     * @param action     网络请求尾址
     * @param params     请求参数
     * @param clazz      返回数据类
     * @param tag        请求复用时的判断标签
     * @param showDialog 是否展示请求加载框
     */
    public <T extends BaseBean> void get(final String action, Map<String, Object> params, Class<T> clazz, Map<String, ?> tag, boolean showDialog, int inputType) {
        if (showDialog) {
            showLoadDialog();
        }

        RetrofitGetService getService = initBaseData(action.substring(0, action.lastIndexOf("/") + 1), inputType).create(RetrofitGetService.class);
        if (params == null) {
            params = new HashMap<>();
        }

        Logger.i("zzz", "request====" + new JSONObject(params));

        getService.getResult(action.substring(action.lastIndexOf("/") + 1), headerParams, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<>(action, clazz, tag, showDialog));
    }

    /**
     * Post请求
     *
     * @param action   请求接口的尾址
     * @param fileList 请求的文件列表
     * @param clazz    要转换的Bean类型（需继承BaseBean）
     * @param <T>      用于继承BaseBean的占位变量
     */
    public <T extends BaseBean> void postFile(String action, List<File> fileList, Class<T> clazz, String fileType) {
        initHeader();
        //防止重置私有云后，不重新创建，导致异常
        RetrofitFileService fileService = initBaseData(action.substring(0, action.lastIndexOf("/") + 1), UPLOAD).create(RetrofitFileService.class);

        Map<String, String> fileHeader = headerParams;
        if (fileHeader.containsKey("Content-Type")) {
            fileHeader.remove("Content-Type");
        }

        List<MultipartBody.Part> partList = filesToMultipartBodyParts(fileList, fileType);

        fileService.fileResult(action.substring(action.lastIndexOf("/") + 1), fileHeader, partList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<>(action, clazz));
    }
    private List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files, String fileType) {
        Logger.i(getClass().getSimpleName(), fileType);
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(fileType), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }
    /**
     * Post请求
     *
     * @param action     网络请求尾址
     * @param params     请求参数
     * @param clazz      返回数据类
     * @param tag        请求复用时的判断标签
     * @param showDialog 是否展示请求加载框
     */
    public <T extends BaseBean> void post(final String action, Map<String, Object> params, Class<T> clazz, Map<String, ?> tag, boolean showDialog, int inputType) {

        if (showDialog) {
            showLoadDialog();
        }

        if (params == null) {
            params = new HashMap<>();
        }

        String requestString = String.valueOf(new JSONObject(params));
        if (TextUtils.isEmpty(requestString) && Const.notEmpty(netRequestCallBack)) {
            //noinspection unchecked
            netRequestCallBack.error(action, new Exception(TxtUtils.getText(mContext, R.string.data_abnormal)), tag);
        }

        RetrofitPostJsonService jsonService = initBaseData(action.substring(0, action.lastIndexOf("/") + 1), inputType).create(RetrofitPostJsonService.class);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestString);

        Logger.i("zzz", "request====" + requestString);

        jsonService.postResult(action.substring(action.lastIndexOf("/") + 1), headerParams, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<>(action, clazz, tag, showDialog));
    }

    /**
     * Post请求
     *
     * @param action     网络请求尾址
     * @param o          请求参数类
     * @param clazz      返回数据类
     * @param tag        请求复用时的判断标签
     * @param showDialog 是否展示请求加载框
     */
    public <T extends BaseBean> void post(final String action, Object o, Class<T> clazz, Map<String, ?> tag, boolean showDialog, int inputType) {

        if (showDialog) {
            showLoadDialog();
        }

        String requestString = new Gson().toJson(o);
        if (TextUtils.isEmpty(requestString) && Const.notEmpty(netRequestCallBack)) {
            //noinspection unchecked
            netRequestCallBack.error(action, new Exception(TxtUtils.getText(mContext, R.string.data_abnormal)), tag);
        }

        RetrofitPostJsonService jsonService = initBaseData(action.substring(0, action.lastIndexOf("/") + 1), inputType).create(RetrofitPostJsonService.class);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestString);

        Logger.i("zzz", "request====" + requestString);

        jsonService.postResult(action.substring(action.lastIndexOf("/") + 1), headerParams, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<>(action, clazz, tag, showDialog));
    }

    public void removeCookies() {
        cookieStore.removeAll();
    }

    public class MyObserver<T extends BaseBean> implements Observer<ResponseBody> {

        private Class<T> clazz;
        private String action;
        boolean showDialog;
        /**
         * 返回结果状态：0、正常Bean；1、Bitmap
         */
        private int resultStatus = 0;
        private Map<String, ?> tag;

        MyObserver(String action, Class<T> clazz, Map<String, ?> tag, boolean showDialog) {
            this.clazz = clazz;
            this.action = action;
            this.tag = tag;
            this.showDialog = showDialog;
        }

        MyObserver(String action, int resultStatus) {
            this.action = action;
            this.resultStatus = resultStatus;
        }

        MyObserver(String action, Class<T> clazz) {
            this.action = action;
            this.clazz = clazz;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @SuppressWarnings("unchecked")
        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            if (showDialog) {
                hideLoadDialog();
            }
            try {
                switch (resultStatus) {
                    case 0:
                        String responseString = responseBody.string();
                        Logger.i("responseString", action + "********** responseString get  " + responseString);
                        if (Const.notEmpty(netRequestCallBack)) {
                            netRequestCallBack.success(action, (T) new Gson().fromJson(responseString, clazz), tag);
                        }
                        break;

                    case 1:
                        if (Const.notEmpty(netRequestCallBack)) {
                            netRequestCallBack.success(action, BitmapFactory.decodeStream(responseBody.byteStream()), tag);
                        }
                        Logger.i("responseString", action + "********** 图片获取成功 ");
                        break;

                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.e(getClass().getSimpleName(), e);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onError(@NonNull Throwable e) {
            if (showDialog) {
                hideLoadDialog();
            }
            try {
                if (e instanceof HttpException) {
                    ResponseBody errorbody = ((HttpException) e).response().errorBody();
                    if (Const.notEmpty(errorbody)) {
                        Logger.i("responseString", String.format("%s********** responseString get error %s content %s", action, e.toString(), TextUtils.isEmpty(errorbody.string()) ? "" : errorbody));
                    }
                } else {
                    Logger.i("responseString", String.format("%s********** responseString get error %s", action, e.toString()));
                }
            } catch (IOException | NullPointerException e1) {
                e1.printStackTrace();
            }
            if (Const.notEmpty(netRequestCallBack)) {
                netRequestCallBack.error(action, e, tag);
            }
        }

        @Override
        public void onComplete() {

        }
    }

    /**
     * 隐藏加载提示框
     */
    public void hideLoadDialog() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bswProgressDialog != null && bswProgressDialog.isShowing()) {
                    bswProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 显示加载提示框
     */
    public void showLoadDialog() {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (bswProgressDialog != null && ! bswProgressDialog.isShowing()) {
                        bswProgressDialog.show();
                    }
                } catch (Exception ex) {
                    StringWriter stackTrace = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stackTrace));
                    Logger.i("Exception", stackTrace.toString());
                }
            }
        });
    }

    public boolean hasToken() {
        return cookieStore.hasCookies();
    }

    /**
     * 网络请求文本结果回调接口
     */
    public abstract static class NetRequestCallBack<TT extends BaseBean> {

        public void success(String action, Bitmap bitmap, Map<String, ?> tag) {

        }

        public void success(String action, File file, Map<String, ?> tag) {

        }

        public abstract void success(String action, TT baseBean, Map<String, ?> tag);

        /**
         * 访问失败回调抽象方法
         *
         * @param action 网络访问尾址
         * @param e      所返回的异常
         * @param tag    当接口复用时，用于区分请求的表识
         */
        public abstract void error(String action, Throwable e, Map<String, ?> tag);
    }
}
