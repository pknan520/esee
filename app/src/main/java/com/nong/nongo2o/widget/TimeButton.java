package com.nong.nongo2o.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nong.nongo2o.AdventurerApp;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 鉴于经常用到获取验证码倒计时按钮 在网上也没有找到理想的 自己写一个
 *
 * @author yung
 *         <p/>
 *         2015年1月14日[佛祖保佑 永无BUG]
 *         <p/>
 *         PS: 由于发现timer每次cancle()之后不能重新schedule方法,所以计时完毕只恐timer.
 *         每次开始计时的时候重新设置timer, 没想到好办法初次下策
 *         注意把该类的onCreate()onDestroy()和activity的onCreate()onDestroy()同步处理
 */
public class TimeButton extends Button implements OnClickListener {
    private long length = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textAfter = "s       ";
    private String textBefore = "获取验证码";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private OnClickListener mOnclickListener;
    private Timer t;
    private TimerTask tt;
    private long time;
    Map<String, Long> map = new HashMap<String, Long>();

    public TimeButton(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText("       " + time / 1000 + textAfter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText(textBefore);
//                TimeButton.this.setBackgroundResource(R.drawable.shape_btn_enable);
                clearTimer();
            }
        }
    };

    private void initTimer() {
        time = length;
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof TimeButton) {
            super.setOnClickListener(l);
        } else
            this.mOnclickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
        initTimer();
        this.setText("       " + time / 1000 + textAfter);
//        this.setBackgroundResource(R.drawable.shape_btn_disable);
        this.setEnabled(false);
        t.schedule(tt, 0, 1000);
        // t.scheduleAtFixedRate(task, delay, period);
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (AdventurerApp.getInstance().sendMsgTime == null)
            AdventurerApp.getInstance().sendMsgTime = new HashMap<String, Long>();
        AdventurerApp.getInstance().sendMsgTime.put(TIME, time);
        AdventurerApp.getInstance().sendMsgTime.put(CTIME, System.currentTimeMillis());
        clearTimer();
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {
        if (AdventurerApp.getInstance().sendMsgTime == null)
            return;
        if (AdventurerApp.getInstance().sendMsgTime.size() <= 0)// 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - AdventurerApp.getInstance().sendMsgTime.get(CTIME) - AdventurerApp.getInstance().sendMsgTime.get(TIME);
        AdventurerApp.getInstance().sendMsgTime.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText("       " + time + textAfter);
//            this.setBackgroundResource(R.drawable.shape_btn_disable);
            this.setEnabled(false);
        }
    }

    /**
     * 设置计时时候显示的文本
     */
    public TimeButton setTextAfter(String text1) {
        this.textAfter = text1;
        return this;
    }

    /**
     * 设置点击之前的文本
     */
    public TimeButton setTextBefore(String text0) {
        this.textBefore = text0;
        this.setText(textBefore);
        return this;
    }

    /**
     * 设置到计时长度
     *
     * @param length 时间 默认毫秒
     * @return
     */
    public TimeButton setLength(long length) {
        this.length = length;
        return this;
    }

}