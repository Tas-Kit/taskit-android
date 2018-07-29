package com.taskit.taskit_android.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import com.taskit.taskit_android.R;

/**
 * Created by nick on 2018/7/12.
 */
public class CountDownButton extends Button {

    //总时长 60s
    private static final long millisinfuture=60000;
    //间隔时长 1s
    private static final long countdowninterval=1000;

    //是否结束
    private boolean isFinish;

    //定时器
    private CountDownTimer countDownTimer;

    public CountDownButton(Context context) {
        this(context, null);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownButton, defStyleAttr, 0);
        //默认为已结束状态
        isFinish = true;
        //字体居中
        setGravity(Gravity.CENTER);
        //默认文字和背景色
        normalBackground();
        //设置定时器
        countDownTimer = new CountDownTimer(millisinfuture, countdowninterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                //未结束
                isFinish = false;
                setText((Math.round((double) millisUntilFinished / 1000) - 1) + "s");
            }

            @Override
            public void onFinish() {
                //结束
                isFinish = true;
                setClickable(true);
                normalBackground();
            }
        };
    }

    private void normalBackground() {
        setText("Send Code");
        setBackgroundResource(R.color.button_send_code);
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void cancel() {
        countDownTimer.cancel();
    }

    public void start() {
        countDownTimer.start();
        setClickable(false);
        setBackgroundResource(R.color.button_send_code_unclickable);
    }

}
