package com.zxycloud.hzy_xg.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.GetBean.GetImageUploadResultBean;
import com.zxycloud.hzy_xg.bean.PostBean.ImagePagerBean;
import com.zxycloud.hzy_xg.bean.PostBean.PostSubmitDeviceExceBean;
import com.zxycloud.hzy_xg.bean.base.ImageAddBean;
import com.zxycloud.hzy_xg.bean.base.ImageUploadItemBean;
import com.zxycloud.hzy_xg.bean.base.SubmitCheckInfoBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.FileUtils;
import com.zxycloud.hzy_xg.utils.GlideUtils;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.Luban;
import com.zxycloud.hzy_xg.utils.MediaFileJudgeUtils;
import com.zxycloud.hzy_xg.utils.PermissionUtils;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.ScreenUtil;
import com.zxycloud.hzy_xg.utils.ToastUtil;
import com.zxycloud.hzy_xg.utils.TxtUtils;
import com.zxycloud.hzy_xg.widget.BswRecyclerView.BswDecoration;
import com.zxycloud.hzy_xg.widget.BswRecyclerView.BswRecyclerView;
import com.zxycloud.hzy_xg.widget.BswRecyclerView.ConvertViewCallBack;
import com.zxycloud.hzy_xg.widget.BswRecyclerView.RecyclerViewHolder;
import com.zxycloud.hzy_xg.widget.CustomProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubmitDeviecExceActivity extends BaseNetActivity {
    private BswRecyclerView<ImageAddBean> pictureList;
    private RelativeLayout addAttachmentsDialog;
    private TextView submitProject;
    private TextView imgDelete;
    private EditText et_exc_describe;
    private final int CAPTURE = 0x633;
    private final int ALBUM = 0x634;
    private int type;
    private String from;
    private final int MAX_COUNT = 9;
    private Spinner spinner_state;
    private String imgPath;
    private SubmitCheckInfoBean submitCheckInfoBean;
    private PostSubmitDeviceExceBean submitDeviceExceBean;
    private String deviceId, taskId;
    private List<ImageAddBean> paths = new ArrayList<>();
    private CustomProgressDialog uploadingProgressDialog;

    @Override
    protected void findViews() {
        pictureList = getView(R.id.picture_list);
        addAttachmentsDialog = getView(R.id.add_attachments_dialog);
        submitProject = getView(R.id.submit_project);
        spinner_state = getView(R.id.spinner_state);
        et_exc_describe = getView(R.id.et_exc_describe);
    }

    @Override
    protected void formatViews() {
        setTitle(R.string.device_submit);
        setBaseRightText(R.string.submit);
        resetBaseBack();
        ImageAddBean imageAddBean = new ImageAddBean(R.drawable.icon_corner_add);
        paths.add(imageAddBean);

        pictureList.initAdapter(R.layout.item_img_rv_layout, convertViewCallBack);
        pictureList.setLayoutManager(BswRecyclerView.VERTICAL, 3);
        pictureList.setDecoration(BswDecoration.ROUND_DECORATION);
        pictureList.setMaxCount(MAX_COUNT);
        pictureList.setData(paths);
        pictureList.scrollViewNesting();

        setOnClickListener(R.id.capture, R.id.add_attachments_dialog, R.id.album);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_attachments_dialog:
                addAttachmentsDialog.setVisibility(View.GONE);
                break;
            case R.id.capture:
                type = CAPTURE;
                PermissionUtils.setRequestPermissions(this, new PermissionUtils.PermissionGrant() {
                    @Override
                    public Integer[] onPermissionGranted() {
                        return new Integer[]{PermissionUtils.CODE_CAMERA};
                    }

                    @Override
                    public void onRequestResult(List<String> deniedPermission) {
                        if (Const.judgeListNull(deniedPermission) == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            imgPath = FileUtils.getNewFileDir(FileUtils.IMAGE_CAPTURE);

                            if (TextUtils.isEmpty(imgPath)) {
                                toast(R.string.file_error);
                                return;
                            }
                            File mPhotoFile = new File(imgPath);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                            startActivityForResult(intent, CAPTURE);
                        } else {
                            toast(R.string.permission_denied);
                        }
                    }
                });
                break;

            // 从相册获取
            case R.id.album:
                PermissionUtils.setRequestPermissions(this, new PermissionUtils.PermissionGrant() {
                    @Override
                    public Integer[] onPermissionGranted() {
                        return new Integer[]{PermissionUtils.CODE_READ_EXTERNAL_STORAGE};
                    }

                    @Override
                    public void onRequestResult(List<String> deniedPermission) {
                        if (Const.judgeListNull(deniedPermission) == 0) {
                            type = ALBUM;
                            Intent inImg = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(inImg, ALBUM);
                        } else {
                            toast(R.string.permission_denied);
                        }
                    }
                });
                break;

            // 上报
            case RIGHT_TEXT_ID:
                if (!et_exc_describe.getText().toString().trim().isEmpty()){
                    showUploadingDialog();
                    uploadImage();
                }else {
                    toast(R.string.exception_submit);
                }
                break;

            case BACK_ID:
                finish();
                break;
        }
    }

    private void uploadImage() {
        int pathSize = Const.judgeListNull(paths);
        if (pathSize > 1) {
            MediaFileJudgeUtils.MediaFileType mediaFileType = null;
            List<File> fileList = new ArrayList<>();
            for (int i = 0; i < pathSize - 1; i++) // pathSize - 2 去掉最后的加号
            {
                String originalPath = paths.get(i).getImgPath();
                String path = Luban.get(mContext).load(new File(originalPath)).launch().getAbsolutePath();
                if (Const.isEmpty(mediaFileType)) {
                    mediaFileType = MediaFileJudgeUtils.getFileType(path);
                }
                if (TextUtils.isEmpty(path)) {
                    continue;
                }
                File file = new File(path);
                fileList.add(file);
                // 拍照的删除，相册不删除
                if (originalPath.contains("/hzy/image")) {
                    FileUtils.delete(originalPath);
                }
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse(originalPath)));
            }
            if (Const.notEmpty(mediaFileType)) {
                postFile(BuildConfig.uploadImg, fileList, GetImageUploadResultBean.class, mediaFileType.mimeType);
            } else {
                toast(R.string.data_anomaly_to_use);
            }
        } else {
            List<ImageUploadItemBean> fileURL = new ArrayList<>();
            if (Const.isEmpty(submitCheckInfoBean))
                submitCheckInfoBean = new SubmitCheckInfoBean();
            submitCheckInfoBean.setImages(fileURL);
            submitExction();
        }
    }

    private void submitExction() {
        String sumbitDescripe = TxtUtils.getText(et_exc_describe);
        if (! TextUtils.isEmpty(sumbitDescripe)) {
            submitCheckInfoBean.setSumbitDescripe(sumbitDescripe);
        }
        if (! submitCheckInfoBean.isAdded()) {
            hideUploadingDialog();
            return;
        }
        PostSubmitDeviceExceBean postSubmitCheckInfoBean = new PostSubmitDeviceExceBean();
        postSubmitCheckInfoBean.setProjectGuid(SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID));
        postSubmitCheckInfoBean.setFaultReason(sumbitDescripe);
        postSubmitCheckInfoBean.setTaskId(taskId);
        postSubmitCheckInfoBean.setDeviceId(deviceId);
        postSubmitCheckInfoBean.setFileUrl(submitCheckInfoBean);
        post(BuildConfig.submitFault, postSubmitCheckInfoBean, BaseBean.class, false, NetUtils.PATROL);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addAttachmentsDialog.setVisibility(View.GONE);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ALBUM:
                    Uri uriImg = data.getData();//得到uri，后面就是将uri转化成file的过程。
                    String imgPath = FileUtils.getFilePathByUri(mContext, uriImg);
                    if (TextUtils.isEmpty(imgPath)) {
                        toast(R.string.file_error);
                        return;
                    }
                    File img = new File(imgPath);
                    this.imgPath = img.toString();
                    paths.add(Const.judgeListNull(paths) - 1, new ImageAddBean(this.imgPath));
                    pictureList.setData(paths);
                    break;

                case CAPTURE:
                    paths.add(Const.judgeListNull(paths) - 1, new ImageAddBean(this.imgPath));
                    pictureList.setData(paths);
                    break;

            }
        }
    }

    /**
     * 图片列表适配器
     */
    private ConvertViewCallBack<ImageAddBean> convertViewCallBack = new ConvertViewCallBack<ImageAddBean>() {
        @Override
        public void convert(RecyclerViewHolder holder, ImageAddBean imageAddBean, final int position) {
            ImageView imgView = holder.getImageView(R.id.item_img);
            ViewGroup.LayoutParams params = imgView.getLayoutParams();
            int imgSize = ScreenUtil.getScreenWidth(mContext) / 3 - ScreenUtil.dp2px(mContext, 10);
            params.height = imgSize;
            params.width = imgSize;
            imgView.setLayoutParams(params);
            TextView deleteImg = holder.getView(R.id.item_delete);
            if (position == Const.judgeListNull(paths) - 1) {
                imgView.setImageResource(imageAddBean.getImgResId());
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAttachmentsDialog.setVisibility(View.VISIBLE);
                    }
                });
                deleteImg.setVisibility(View.GONE);
            } else {
                GlideUtils.loadImageView(mContext, imageAddBean.getImgPath(), imgView);
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<ImageUploadItemBean> images = new ArrayList<>();
                        for (int i = 0; i < Const.judgeListNull(paths) - 1; i++) {
                            String imagePath = paths.get(i).getImgPath();
                            if (TextUtils.isEmpty(imagePath)) {
                                continue;
                            }
                            images.add(new ImageUploadItemBean(imagePath));
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImagePagerActivity.EXTRA_IMAGE_URLS, new ImagePagerBean(images));
                        bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                        jumpTo(ImagePagerActivity.class, bundle);
                    }
                });
                deleteImg.setVisibility(View.VISIBLE);
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paths.remove(position);
                        pictureList.removeItem(position);
                        pictureList.requestLayout();
                    }
                });
            }
        }
    };

    /**
     * 隐藏加载提示框
     */
    public void hideUploadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uploadingProgressDialog != null && uploadingProgressDialog.isShowing()) {
                    uploadingProgressDialog.dismiss();
                }
            }
        });
    }

    public void showUploadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 由于setMessage中textView在此时会为空，所以重新创建，修复bug后可重新使用
                    if (Const.isEmpty(uploadingProgressDialog)) {
                        uploadingProgressDialog = new CustomProgressDialog(mActivity, R.style.progress_dialog_loading, R.string.is_uploading);
                    }
                    if (!uploadingProgressDialog.isShowing()) {
                        uploadingProgressDialog.show();
                    }
                } catch (Exception ex) {
                    Logger.e("Exception", ex);
                }
            }
        });
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        if (baseBean.isSuccessful()) {
            switch (action) {
                case BuildConfig.uploadImg:
                    List<ImageUploadItemBean> fileURL = ((GetImageUploadResultBean) baseBean).getData().getFileURL();
                    if (Const.isEmpty(submitCheckInfoBean))
                        submitCheckInfoBean = new SubmitCheckInfoBean();
                    submitCheckInfoBean.setImages(fileURL);
                    submitExction();
                    break;
                case BuildConfig.submitFault:
                    hideUploadingDialog();
                    if (baseBean.isSuccessful()){
                        backTo(DeviceListActivity.class);
                    }
                    toast(baseBean.getMessage());
                    break;
            }
        } else {
            hideUploadingDialog();
            toast(baseBean.getMessage());
        }
    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {
        hideUploadingDialog();
        toast(errorString);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_submit_exc;
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            deviceId = bundle.getString("deviceId");
            taskId = bundle.getString("taskId");
            from = bundle.getString("from");
        }
    }
    @Override
    protected void formatData() {}
}
