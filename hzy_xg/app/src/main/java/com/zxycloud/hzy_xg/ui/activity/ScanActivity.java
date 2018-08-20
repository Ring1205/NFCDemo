package com.zxycloud.hzy_xg.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zxycloud.hzy_xg.BuildConfig;
import com.zxycloud.hzy_xg.R;
import com.zxycloud.hzy_xg.base.BaseBean;
import com.zxycloud.hzy_xg.base.activity.BaseNetActivity;
import com.zxycloud.hzy_xg.netWork.NetUtils;
import com.zxycloud.hzy_xg.utils.Const;
import com.zxycloud.hzy_xg.utils.NfcUtils;
import com.zxycloud.hzy_xg.utils.SPUtils;
import com.zxycloud.hzy_xg.utils.TimerUtils;
import com.zxycloud.hzy_xg.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class ScanActivity extends BaseNetActivity {
    private String nfcID, labelID;
    private String projectGuid;
    private String jump;
    private String taskId;
    private boolean isNFC;
    private Button bt_scan;
    private ProgressBar pb_scan;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("NFC扫描");
        //nfc初始化设置
        isNFC = NfcUtils.getInitialize(this);
        projectGuid = SPUtils.getInstance(mContext).getString(SPUtils.PROJECT_ID);
        getView(R.id.base_title).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (taskId != null){
                    bt_scan.setText("假装扫描 (ง•_•)???");
                }else {
                    toast("装不了 ㄟ( ▔, ▔ )ㄏ");
                }
                return false;
            }
        });
    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            jump = bundle.getString("jump");
            taskId = bundle.getString("taskId");
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_scan:
                //开启前台调度系统
                if (isNFC) {
                    pb_scan.setVisibility(View.VISIBLE);
                    bt_scan.setText("扫描中...");
                    openTimer();
                    NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
                } else if (bt_scan.getText().toString().contains("假装")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", taskId);
                    bundle.putString("type", "device");
                    jumpTo(DeviceListActivity.class, bundle);
                } else {
                    Toast.makeText(this, "该设备不支持NFC", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    TimerUtils timerUtils;

    private void openTimer() {
        timerUtils = new TimerUtils(3600000, 120000, new TimerUtils.OnBaseTimerCallBack() {
            @Override
            public void onTick(long millisUntilFinished) {
                System.gc();
            }

            @Override
            public void onFinish() {
                bt_scan.setText("重新扫描");
                //关闭前台调度系统
                if (isNFC) {
                    NfcUtils.mNfcAdapter.disableForegroundDispatch(ScanActivity.this);
                }
            }
        });
        timerUtils.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pb_scan.setVisibility(View.INVISIBLE);
        bt_scan.setText("开始扫描");
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void stopTimer() {
        if (timerUtils != null)
            timerUtils.stop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcID = NfcUtils.readNFCId(intent);
        labelID = NfcUtils.readNFCFromTag(intent);
        if (!nfcID.isEmpty()) {
            if ("add".equals(jump) || "scan".equals(jump)) {
                params = new HashMap<>();
                params.put("markId", labelID);
                get(BuildConfig.isExist, BaseBean.class, false, NetUtils.PATROL);
            } else if (labelID.equals(jump) && taskId != null) {
                Bundle bundle = new Bundle();
                bundle.putString("taskId", taskId);
                bundle.putString("type", "device");
                jumpTo(DeviceListActivity.class, bundle);
            } else {
                ToastUtil.showLongToast(this, "校验失败，请核实任务标签");
                finish();
            }
        }
    }

    @Override
    protected void findViews() {
        pb_scan = getView(R.id.pb_scan);
        bt_scan = getView(R.id.bt_scan);
        if (taskId != null) {
            bt_scan.setText(R.string.scan_task_list);
        }
    }

    @Override
    protected void formatViews() {
        pb_scan.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void success(String action, BaseBean baseBean, Map<String, ?> tag) {
        switch (action) {
            case BuildConfig.isExist:
                if (baseBean.isSuccessful() && "add".equals(jump)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("projectGuid", projectGuid);
                    bundle.putString("nfcID", nfcID);
                    bundle.putString("labelID", labelID);
                    jumpTo(AddLabelActivity.class, bundle);
                } else if ("scan".equals(jump) && "P00001".equals(baseBean.getCode())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("labelID", labelID);
                    jumpTo(TaskListActivity.class, bundle);
                } else {
                    Toast.makeText(this, baseBean.getMessage(this), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void error(String action, String errorString, Map<String, ?> tag) {

    }
}
