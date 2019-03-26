package com.heaven7.vida.research;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.SeekBar;

import androidx.dynamicanimation.animation.SpringAnimation;

public class TestAndroidSpringActivity extends Activity implements View.OnTouchListener {

    private SeekBar damping, stiffness;
    private View box;

    private float downX, downY;
    private VelocityTracker velocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(android.R.id.content).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        stiffness = (SeekBar) findViewById(R.id.stiffness);
        damping = (SeekBar) findViewById(R.id.damping);
        box = findViewById(R.id.box);

        velocityTracker = VelocityTracker.obtain();
        findViewById(R.id.root).setOnTouchListener(this);
    }

    private float getStiffness() {
        return Math.max(stiffness.getProgress(), 1f);
    }

    private float getDamping() {
        return damping.getProgress() / 100f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                velocityTracker.addMovement(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                box.setTranslationX(event.getX() - downX);
                box.setTranslationY(event.getY() - downY);
                velocityTracker.addMovement(event);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.computeCurrentVelocity(1000);
               /* if (box.getTranslationX() != 0) {
                    SpringAnimation animX = new SpringAnimation(box, SpringAnimation.TRANSLATION_X, 0);
                    animX.getSpring().setStiffness(getStiffness());
                    animX.getSpring().setDampingRatio(getDamping());
                    animX.setStartVelocity(velocityTracker.getXVelocity());
                    animX.start();
                }*/
                if (box.getTranslationY() != 0) {
                    SpringAnimation animY = new SpringAnimation(box, SpringAnimation.TRANSLATION_Y, 0);
                    animY.getSpring().setStiffness(getStiffness());
                    animY.getSpring().setDampingRatio(getDamping());
                    animY.setStartVelocity(velocityTracker.getYVelocity());
                    animY.start();

                 /*   FlingAnimation  fa = new FlingAnimation(box, FlingAnimation.TRANSLATION_Y);
                    fa.setFriction(1);
                    fa.setMinValue(0);
                    fa.setMaxValue(1000f);
                    fa.setStartVelocity(1000f);
                    fa.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                        @Override
                        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                            Log.d("fling", value + "/" + velocity + "/" + (velocity + 4.2f * value - 1000f)); // DEFAULT_FRICTION = 4.2f
                        }
                    });
                    fa.start();*/
                }
                velocityTracker.clear();
                return true;
        }
        return false;
    }

}
