package com.zxycloud.hzy_xg.utils;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.csf.lame4android.utils.FLameUtils;
import com.zxycloud.hzy_xg.R;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class RecordingUtils {
    public static final int NUM_CHANNELS = 1;
    public static final int SAMPLE_RATE = 16000;
    public static final int BITRATE = 128;
    public static final String ERROR = "ERROR";

    private Context context;
    private MyHandler myHandler;
    private MediaPlayer mediaPlayer;//播放器
    private TimerUtils timerUtils;//定时器
    private MediaPlayer.OnCompletionListener onCompletionListener;//播放监听
    private RecordingListener recordingListener;
    private short[] mBuffer;
    private AudioRecord mRecorder;
    private boolean mIsRecording = false;
    private File rawFile, mp3File;

    public RecordingUtils(Context context) {
        this.context = context;
        // 语音播放定时器
        myHandler = new MyHandler(this);
    }

    public RecordingUtils(Context context, TimerUtils timerUtils, RecordingListener recordingListener) {
        this.context = context;
        this.timerUtils = timerUtils;
        this.recordingListener = recordingListener;
        initRecorder();
        // 语音播放定时器
        myHandler = new MyHandler(this);
    }

    /**
     * 开始录音
     */
    public void start() {
        mIsRecording = true;
        mRecorder.startRecording();
        recordData();
    }

    /**
     * 停止录音
     */
    public void stopRecording() {
        recordingListener.onRunRawToMp3();
        FLameUtils fLameUtils = new FLameUtils(NUM_CHANNELS, SAMPLE_RATE, BITRATE);
        mIsRecording = false;
        if (Const.notEmpty(mRecorder)) {
            mRecorder.stop();
            mp3File = new File(rawFile.getAbsolutePath().replace(".raw", ".mp3"));
            boolean result = fLameUtils.raw2mp3(rawFile.getAbsolutePath(), mp3File.getAbsolutePath());
            recordingListener.onRecordResult(mp3File.getAbsolutePath());
        }
    }

    /**
     * 开始播放
     */
    public void play() {
        if (mp3File != null && mp3File.exists()) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mp3File.getAbsolutePath());
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
            Toast.makeText(context, R.string.file_error, Toast.LENGTH_SHORT).show();
        }
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
     * 播放状态判断
     *
     * @return 是否还在播放
     */
    public boolean isPlaying() {
        if (Const.isEmpty(mediaPlayer))
            return false;
        return mediaPlayer.isPlaying();
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
         * rww正在转mp3时
         */
        void onRunRawToMp3();

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

    /**
     * 初始化录音机
     */
    private void initRecorder() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mBuffer = new short[bufferSize];
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
    }

    //记录数据
    public void recordData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeData();
            }
        }).start();
    }

    public void writeData() {
        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(rawFile = FileUtils.getNewFile(FileUtils.RAW_RECORD))));
            while (mIsRecording) {
                int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
                for (int i = 0; i < readSize; i++) {
                    output.writeShort(mBuffer[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void playUrl(final String url) {
        if (!TextUtils.isEmpty(url)) {
            mediaPlayer = new MediaPlayer();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        while (Const.notEmpty(mediaPlayer) && mediaPlayer.isPlaying()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                myHandler.obtainMessage();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.file_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();
            if (Const.notEmpty(onCompletionListener))
                mediaPlayer.setOnCompletionListener(onCompletionListener);
        } else {
            Toast.makeText(context, R.string.file_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 音频播放定时器
     */
    private static class MyHandler extends Handler {
        WeakReference<RecordingUtils> recordingUtilsWeakReference;

        public MyHandler(RecordingUtils recordingUtils) {
            recordingUtilsWeakReference = new WeakReference<>(recordingUtils);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecordingUtils recordingUtils = recordingUtilsWeakReference.get();
            if (Const.notEmpty(recordingUtils)) {
                MediaPlayer mediaPlayer = recordingUtils.mediaPlayer;
                if (Const.notEmpty(mediaPlayer) && mediaPlayer.isPlaying() && Const.notEmpty(recordingUtils.recordingListener)) {
                    recordingUtils.recordingListener.getCurrentProgress(mediaPlayer.getCurrentPosition());
                }
            }
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

}
