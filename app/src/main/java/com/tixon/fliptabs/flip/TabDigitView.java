package com.tixon.fliptabs.flip;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.tixon.fliptabs.R;

/**
 * Created by tikhon.osipov on 11.05.17
 */

public class TabDigitView extends FrameLayout {
    private TabDigit digit;
    private int valueStart, valueMoveTo;
    private ValueAnimator valueAnimator;

    //digit to display with view creation without animation
    public void setValueStart(int valueStart) {
        this.valueStart = valueStart;

        //first initialization
        if(digit != null) {
            digit.mTopTab.setChar(valueStart);
            digit.mMiddleTab.setChar(valueStart);
            digit.mBottomTab.setChar(valueStart);
            digit.invalidate();
        }

    }

    //digit to animate to
    public void setValueMoveTo(int valueMoveTo) {
        this.valueMoveTo = valueMoveTo;
    }

    public TabDigitView(@NonNull Context context) {
        super(context);
        init();
    }

    public TabDigitView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabDigitView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public TabDigitView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.tab_digit_view_layout, this, true);

        digit = (TabDigit) view.findViewById(R.id.tabDigit);
        digit.setDividerColor(Color.parseColor("#111111"));

        final FrameLayout container = (FrameLayout) view.findViewById(R.id.digitContainer);
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

                digit.mTopTab.setChar(valueMoveTo);
                digit.mMiddleTab.setChar(valueStart);
                digit.mBottomTab.setChar(valueStart);
                if(angle < 90) {
                    digit.mMiddleTab.setChar(valueMoveTo);
                }

                digit.invalidate();
            }
        });
        valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
    }

    public void flip() {
        if(valueAnimator != null) valueAnimator.start();
    }
}
