package com.zxycloud.hzy_xg.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.csf.lame4android.utils.FLameUtils;
import com.zxycloud.hzy_xg.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

/**
 * @author leiming
 * @date 2018/6/23.
 */

public class RecordingMediaRecordUtils {
    /**
     * 时间显示顺序：ASC：顺序   DESC：倒序
     */
    public static final int NUM_CHANNELS = 1;
    public static final int SAMPLE_RATE = 16000;
    public static final int BITRATE = 128;
    public static final int ASC = 0x011;
    public static final int DESC = 0x012;
    private RecordingListener recordingListener;

    private File amrFile;
    private File mp3File;
    //文件输出流
    private OutputStream os;

    //记录播放状态
    private boolean isRecording = false;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 多媒体播放器
     */
    private MediaPlayer mediaPlayer;
    /**
     * 音频录制
     */
    private MediaRecorder mediaRecorder;
    /**
     * 显示时间的类型
     */
    private int showTimeType = DESC;
    /**
     * 定时器
     */
    private TimerUtils timerUtils;

    private boolean isCancel = false;

    private MyHandler myHandler;
    private MediaPlayer.OnCompletionListener onCompletionListener;

    public RecordingMediaRecordUtils(Context context) {
        this.context = context;
        // 语音播放定时器
        myHandler = new MyHandler(this);
    }

    public RecordingMediaRecordUtils(Context context, TimerUtils timerUtils, RecordingListener recordingListener) {
        this.context = context;
        this.timerUtils = timerUtils;
        this.recordingListener = recordingListener;
        mediaRecorder = new MediaRecorder();
        // 语音播放定时器
        myHandler = new MyHandler(this);
    }

    /**
     * 开始录音
     */
    public void start() {
        if (isRecording) {
            stopRecording();
            return;
        }
        isRecording = true;
        recordData();
    }


    /**
     * 停止播放
     */
    public void stopPlaying() {
        if (Const.notEmpty(mediaPlayer) && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
    }

    /**
     * 停止录制
     */
    public void stopRecording() {
        isRecording = false;
        mediaRecorder.stop();
        FLameUtils fLameUtils = new FLameUtils(NUM_CHANNELS, SAMPLE_RATE, BITRATE);
        mp3File = new File(amrFile.getAbsolutePath().replace(".amr", ".mp3"));
        boolean result = fLameUtils.raw2mp3(amrFile.getAbsolutePath(), mp3File.getAbsolutePath());
        Logger.e("Guke", "result:"+result+"   path:"+mp3File.getAbsolutePath());
//        recordingListener.onRecordResult(amrFile.getAbsolutePath());
        recordingListener.onRecordResult(mp3File.getAbsolutePath());
    }

    /**
     * 播放状态判断
     *
     * @return 是否还在播放
     */
    public boolean isPlaying() {
        if (Const.isEmpty(mediaPlayer)) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    public void playUrl(String url) {
        if (! TextUtils.isEmpty(url)) {
            mediaPlayer = new MediaPlayer();
            // 为播放器设置数据文件
            // 准备并且启动播放器
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (Const.notEmpty(mediaPlayer) && mediaPlayer.isPlaying()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                myHandler.obtainMessage();
                            }
                        }
                    }
                }).start();
                if (Const.notEmpty(recordingListener)) {
                    recordingListener.getMaxProgress(mediaPlayer.getDuration());
                    if (Const.notEmpty(onCompletionListener))
                        mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            } catch (IOException e) {
                e.printStackTrace();
                toast(R.string.file_error);
            }
        } else {
            toast(R.string.file_error);
        }
    }

    /**
     * 播放
     */
    public void play() {
        if (amrFile != null && amrFile.exists()) {
            mediaPlayer = new MediaPlayer();
            // 为播放器设置数据文件
            // 准备并且启动播放器
            try {
                mediaPlayer.setDataSource(amrFile.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (Const.notEmpty(mediaPlayer) && mediaPlayer.isPlaying()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                myHandler.obtainMessage();
                            }
                        }
                    }
                }).start();
                recordingListener.getMaxProgress(mediaPlayer.getDuration());
                if (Const.notEmpty(onCompletionListener))
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
            } catch (IOException e) {
                e.printStackTrace();
                recordingListener.onRecordError(e);
            }
        } else {
            toast(R.string.file_error);
        }
    }

    /**
     * 取消录制
     */
    public void cancel() {
        isCancel = true;
    }

    /**
     * 播放文件
     *
     * @param file 所要播放的文件
     */
    public void play(File file) {
        if (file != null) {
            amrFile = file;
            play();
        } else {
            toast(R.string.file_error);
        }
    }

    /**
     * 播放文件
     *
     * @param filePath 所要播放文件的路径
     */
    public void play(String filePath) {
        if (! TextUtils.isEmpty(filePath)) {
            amrFile = new File(filePath);
            play();
        } else {
            toast(R.string.file_error);
        }
    }

    /**
     * 播放完成监听方法设置
     *
     * @param onCompletionListener 播放完成监听
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    /**
     * 显示提示文本
     *
     * @param message 显示信息的资源ID
     */
    private void toast(int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 音频播放定时器
     */
    private static class MyHandler extends Handler {
        WeakReference<RecordingMediaRecordUtils> recordingUtilsWeakReference;

        public MyHandler(RecordingMediaRecordUtils recordingUtils) {
            recordingUtilsWeakReference = new WeakReference<>(recordingUtils);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecordingMediaRecordUtils recordingUtils = recordingUtilsWeakReference.get();
            if (Const.notEmpty(recordingUtils)) {
                MediaPlayer mediaPlayer = recordingUtils.mediaPlayer;
                if (Const.notEmpty(mediaPlayer) && mediaPlayer.isPlaying() && Const.notEmpty(recordingUtils.recordingListener)) {
                    recordingUtils.recordingListener.getCurrentProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }
    }

    /**
     * 录音监听
     */
    public interface RecordingListener {
        /**
         * 录音结果返回
         *
         * @param recordPath 录音文件路径
         */
        void onRecordResult(String recordPath);

        /**
         * 用户取消录音
         */
        void onCancel();

        /**
         * 录音过程报错
         *
         * @param e 错误日志
         */
        void onRecordError(Exception e);

        /**
         * 录音播放时文件最大时长
         *
         * @param maxProgress 播放录音最大时长
         */
        void getMaxProgress(int maxProgress);

        /**
         * 进度监听
         *
         * @param currentProgress 当前播放进度
         */
        void getCurrentProgress(int currentProgress);
    }

    //记录数据
    public void recordData() {
        new Thread(new WriteThread()).start();
    }

    //读取录音数字数据线程
    class WriteThread implements Runnable {
        public void run() {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            amrFile = FileUtils.getNewFile(FileUtils.AMR_RECORD);
            if (Const.notEmpty(amrFile)) {
                mediaRecorder.setOutputFile(amrFile.getAbsolutePath());
            } else {
                toast(R.string.data_anomaly_to_use);
            }
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
                toast(R.string.data_anomaly_to_use);
            }
        }
    }

}
