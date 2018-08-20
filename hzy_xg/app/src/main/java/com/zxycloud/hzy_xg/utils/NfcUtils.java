package com.zxycloud.hzy_xg.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.provider.Settings;
import android.widget.Toast;

import com.zxycloud.hzy_xg.App;

import java.io.UnsupportedEncodingException;

public class NfcUtils {

    //nfc  
    public static NfcAdapter mNfcAdapter;
    public static IntentFilter[] mIntentFilter = null;
    public static PendingIntent mPendingIntent = null;
    public static String[][] mTechList = null;  

    /** 
     * 构造函数，用于初始化nfc
     */  
    private NfcUtils() {
    }

    public static boolean getInitialize(Activity activity){
        mNfcAdapter = NfcCheck(activity);
        NfcInit(activity);
        return mNfcAdapter != null;
    }

    /** 
     * 检查NFC是否打开 
     */  
    public static NfcAdapter NfcCheck(Activity activity) {  
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);  
        if (mNfcAdapter == null) {  
            return null;  
        } else {  
            if (!mNfcAdapter.isEnabled()) {  
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                activity.startActivity(setNfc);  
            }  
        }  
        return mNfcAdapter;  
    }  

    /** 
     * 初始化nfc设置 
     */  
    public static void NfcInit(Activity activity) {  
        mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);  
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);  
        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);  
        try {  
            filter.addDataType("*/*");  
        } catch (IntentFilter.MalformedMimeTypeException e) {  
            e.printStackTrace();  
        }  
        mIntentFilter = new IntentFilter[]{filter, filter2};  
        mTechList = null;  
    }  

    /**  
     * 读取NFC的数据  
     */  
    public static String readNFCFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {  
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                String readResult = null;
                try {
                    readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return readResult;  
            }  
        }  
        return "";  
    }  

    /**
     * 往标签写数据的方法
     *
     * @param intent
     */
    public static void writeNFCTag(Intent intent, String something) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{NdefRecord
                .createApplicationRecord(something)});
        //转换成字节获得大小
        int size = ndefMessage.toByteArray().length;
        try {
            //2.判断NFC标签的数据类型（通过Ndef.get方法）
            Ndef ndef = Ndef.get(tag);
            //判断是否为NDEF标签
            if (ndef != null) {
                ndef.connect();
                //判断是否支持可写
                if (!ndef.isWritable()) {
                    return;
                }
                //判断标签的容量是否够用
                if (ndef.getMaxSize() < size) {
                    return;
                }
                //3.写入数据
                ndef.writeNdefMessage(ndefMessage);
                Toast.makeText(App.getInstance(), "写入成功", Toast.LENGTH_SHORT).show();
            } else { //当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
                //Ndef格式类
                NdefFormatable format = NdefFormatable.get(tag);
                //判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
                if (format != null) {
                    //连接
                    format.connect();
                    //格式化并将信息写入标签
                    format.format(ndefMessage);
                    Toast.makeText(App.getInstance(), "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.getInstance(), "写入失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(App.getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取ndef对象
     * @return
     */
    public static Ndef getNfcF(Intent intent){
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        return Ndef.get(detectedTag);
    }
    /**
     * 读取nfcID
     */
    public static String readNFCId(Intent intent){
        return ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
    }
    /**
     * 将字节数组转换为字符串 
     */  
    private static String ByteArrayToHexString(byte[] inarray) {  
        int i, j, in;  
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};  
        String out = "";  

        for (j = 0; j < inarray.length; ++j) {  
            in = (int) inarray[j] & 0xff;  
            i = (in >> 4) & 0x0f;  
            out += hex[i];  
            i = in & 0x0f;  
            out += hex[i];  
        }  
        return out;  
    }  
}  