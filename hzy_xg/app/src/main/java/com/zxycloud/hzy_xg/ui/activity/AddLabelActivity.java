package com.zxycloud.hzy_xg.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.bean.base.ImageAddBean;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.FileUtils;
import com.zxycloud.hzy_xg.utils.GlideUtils;
import com.zxycloud.hzy_xg.utils.Logger;
import com.zxycloud.hzy_xg.utils.PermissionUtils;
import com.zxycloud.hzy_xg.utils.ScreenUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddLabelActivity extends BaseNetActivity {
    private final int CAPTURE = 0x633;
    String projectGuid, nfcID, labelID, imgPath;
    EditText et_table_address, et_table_name;
    ImageView item_img;
    TextView item_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("填写标签信息");
    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case "mark/addMark":
                if (baseBean.isSuccessful()) {
                    backTo(MainActivity.class);
                } else {
                    Toast.makeText(this, baseBean.getMessage(this), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {}

    private void addMark() {
        Map<String, String> params = new HashMap<>();
        params.put("id", labelID);
        params.put("rid", nfcID);
        params.put("projectGuid", projectGuid);
        params.put("name", et_table_name.getText().toString().trim());
        params.put("imgurl", imgPath != null ? imgPath : "");
        params.put("addressDescribation", et_table_address.getText().toString().trim());
        post("mark/addMark", params, BaseBean.class, true, NetUtils.PATROL);
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            projectGuid = bundle.getString("projectGuid");
            nfcID = bundle.getString("nfcID");
            labelID = bundle.getString("labelID");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_tabel;
    }

    @Override
    protected void findViews() {
        et_table_address = getView(R.id.et_table_address);
        et_table_name = getView(R.id.et_table_name);
        item_img = getView(R.id.item_img);
        item_delete = getView(R.id.item_delete);
        setOnClickListener(R.id.item_delete, R.id.item_img);
    }

    @Override
    protected void formatViews() {
        Logger.i("label", "nfcID:" + nfcID + "\nprojectGuid:" + projectGuid);
        item_delete.setVisibility(View.GONE);
    }

    @Override
    protected void formatData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_scan:
                if (!et_table_name.getText().toString().trim().isEmpty()) {
                    addMark();
                } else {
                    Toast.makeText(this, "请填写标签名称", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.item_img:
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
            case R.id.item_delete:
                item_img.setImageResource(R.drawable.icon_corner_add);
                item_delete.setVisibility(View.GONE);
                imgPath = "";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAPTURE:
                    GlideUtils.loadImageView(mContext, imgPath, item_img);
                    item_delete.setVisibility(View.VISIBLE);
                    break;

            }
        }
    }

}
