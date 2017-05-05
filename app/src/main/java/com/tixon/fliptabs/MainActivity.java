package com.tixon.fliptabs;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;

import com.tixon.fliptabs.flip.TabDigit;

import static com.tixon.fliptabs.flip.TabDigit.LOWER_POSITION;
import static com.tixon.fliptabs.flip.TabDigit.MIDDLE_POSITION;
import static com.tixon.fliptabs.flip.TabDigit.UPPER_POSITION;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabDigit digit = (TabDigit) findViewById(R.id.tabDigit1);

        ValueAnimator valueanimator = ValueAnimator.ofFloat(0.0F, 180.0F);
        valueanimator.setDuration(1000L);
        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float) animation.getAnimatedValue();
                digit.mMiddleTab.rotate((int) angle);

                //flipping from bottom to top
                switch (digit.getState()) {
                    case LOWER_POSITION: {
                        digit.nextBottomTab();
                        break;
                    }
                    case MIDDLE_POSITION: {
                        if (angle > 90) {
                            digit.nextMiddleTab();
                        }
                        break;
                    }
                    case UPPER_POSITION: {
                        if (angle >= 180) {
                            digit.nextTopTab();
                        }
                        break;
                    }
                }

                digit.invalidate();
            }
        });
        valueanimator.setStartDelay(1000L);
        valueanimator.setRepeatCount(5);
        valueanimator.setInterpolator(new LinearInterpolator());
        valueanimator.start();
    }
}
