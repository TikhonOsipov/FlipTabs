package com.tixon.fliptabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.tixon.fliptabs.flip.TabDigit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int index = 0;
    private ValueAnimator valueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabDigit digit = (TabDigit) findViewById(R.id.tabDigit1);
        digit.setDividerColor(Color.parseColor("#111111"));

        final FrameLayout container = (FrameLayout) findViewById(R.id.digitContainer1);
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int maxWidth = digit.mMiddleTab.maxWith();
                int width = digit.getViewWidth();
                int padding = (maxWidth - width);
                ViewGroup.LayoutParams containerParams = container.getLayoutParams();
                int layoutPadding = getResources().getDimensionPixelSize(R.dimen.digitPadding);
                containerParams.height = digit.getHeight() + layoutPadding;
                containerParams.width = maxWidth - padding + layoutPadding;
                container.setLayoutParams(containerParams);
                container.invalidate();
            }
        });

        valueAnimator = ValueAnimator.ofFloat(180.0F, 0.0F);
        valueAnimator.setDuration(1000L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float) animation.getAnimatedValue();
                digit.mMiddleTab.rotate((int) angle);

                digit.mTopTab.setChar((index+1)%10);
                digit.mMiddleTab.setChar(index%10);
                digit.mBottomTab.setChar(index%10);
                if(angle < 90) {
                    digit.mMiddleTab.setChar((index+1)%10);
                }

                digit.invalidate();
            }
        });
        valueAnimator.setRepeatCount(20);
        valueAnimator.setInterpolator(new BounceInterpolator());


        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setButtonsEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                index++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setButtonsEnabled(true);
            }
        });

        findViewById(R.id.bounce).setOnClickListener(this);
        findViewById(R.id.linear).setOnClickListener(this);
        findViewById(R.id.accelerateDecelerate).setOnClickListener(this);
        findViewById(R.id.anticipate).setOnClickListener(this);
        findViewById(R.id.anticipateOvershoot).setOnClickListener(this);
        findViewById(R.id.overshoot).setOnClickListener(this);
        findViewById(R.id.accelerate).setOnClickListener(this);
        findViewById(R.id.decelerate).setOnClickListener(this);
        findViewById(R.id.fastOutSlow).setOnClickListener(this);

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueAnimator.end();
                setButtonsEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bounce:
                valueAnimator.setInterpolator(new BounceInterpolator());
                break;
            case R.id.linear:
                valueAnimator.setInterpolator(new LinearInterpolator());
                break;
            case R.id.accelerateDecelerate:
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case R.id.anticipate:
                valueAnimator.setInterpolator(new AnticipateInterpolator());
                break;
            case R.id.anticipateOvershoot:
                valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
                break;
            case R.id.overshoot:
                valueAnimator.setInterpolator(new OvershootInterpolator());
                break;
            case R.id.accelerate:
                valueAnimator.setInterpolator(new AccelerateInterpolator());
                break;
            case R.id.decelerate:
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                break;
            case R.id.fastOutSlow:
                valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
                break;
        }
        index = 0;
        valueAnimator.start();
    }

    private void setButtonsEnabled(boolean enabled) {
        findViewById(R.id.bounce).setEnabled(enabled);
        findViewById(R.id.linear).setEnabled(enabled);
        findViewById(R.id.accelerateDecelerate).setEnabled(enabled);
        findViewById(R.id.anticipate).setEnabled(enabled);
        findViewById(R.id.anticipateOvershoot).setEnabled(enabled);
        findViewById(R.id.overshoot).setEnabled(enabled);
        findViewById(R.id.accelerate).setEnabled(enabled);
        findViewById(R.id.decelerate).setEnabled(enabled);
        findViewById(R.id.fastOutSlow).setEnabled(enabled);
    }
}
