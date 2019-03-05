package com.heaven7.vida.research.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.heaven7.core.util.MainWorker;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.service.TestAccessibilityService;
import com.heaven7.vida.research.utils.AccessibilityUtil;

import java.util.Timer;
import java.util.TimerTask;

public class AccessibilityOpenHelperActivity extends AppCompatActivity {
    private boolean isFirstCome = true;
    private static final String ACTION = "action";
    private static final String ACTION_FINISH_SELF = "action_finis_self";

    private Timer timer;
    private TimerTask timerTask;
    private int mTimeoutCounter = 0;

    private int TIMEOUT_MAX_INTERVAL = 60 * 2; // 2 min

    private long TIMER_CHECK_INTERVAL = 1000;
    protected static Runnable tipToastDelayRunnable;

    private static void removeDelayedToastTask() {
        if (tipToastDelayRunnable != null) {
            MainWorker.remove(tipToastDelayRunnable);
            tipToastDelayRunnable = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_accessibility_transparent);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String action = intent.getStringExtra(ACTION);
            if (ACTION_FINISH_SELF.equals(action)) {
                finishCurrentActivity();
                return;
            }
        }
        mTimeoutCounter = 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishCurrentActivity();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null) {
            String action = intent.getStringExtra(ACTION);
            if (ACTION_FINISH_SELF.equals(action)) {
                finishCurrentActivity();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstCome) {
            removeDelayedToastTask();
            finishCurrentActivity();
        } else {
            jumpActivities();
            startCheckAccessibilityOpen();
        }
        isFirstCome = false;
    }

    private void jumpActivities() {
        try {
            Intent intent = AccessibilityUtil.getAccessibilitySettingPageIntent(this);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        freeTimeTask();
        super.onDestroy();
    }

    private void finishCurrentActivity() {
        freeTimeTask();
        finish();
    }

    private void startCheckAccessibilityOpen() {
        freeTimeTask();
        initTimeTask();
        timer.schedule(timerTask, 0, TIMER_CHECK_INTERVAL);
    }

    private void initTimeTask() {
        timer = new Timer();
        mTimeoutCounter = 0;
        timerTask = new TimerTask() {

            @SuppressWarnings("static-access")
            @Override
            public void run() {
                if (AccessibilityUtil.isAccessibilitySettingsOn(AccessibilityOpenHelperActivity.this)) {
                    freeTimeTask();
                    Looper.prepare();
                    try {
                        AccessibilityOpenHelperActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AccessibilityOpenHelperActivity.this, "辅助功能开启成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent();
                        intent.putExtra(ACTION, ACTION_FINISH_SELF);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(AccessibilityOpenHelperActivity.this, AccessibilityOpenHelperActivity.this.getClass());
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Looper.loop();
                }
                // 超过2分钟超时，就释放timer。
                mTimeoutCounter++;
                if (mTimeoutCounter > TIMEOUT_MAX_INTERVAL) {
                    freeTimeTask();
                }
            }
        };
    }

    private void freeTimeTask() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}