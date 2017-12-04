package com.heaven7.study.android_arch;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.core.util.Logger;


public class MainActivity extends AppCompatActivity implements LifecycleOwner{

    final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register();
        ReportFragment.injectIfNeededIn(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }

    private void register() {
        registry.addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            public void onCreate(){
                Logger.i("MainActivity","onCreate","");

            }
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public void onStart(){
 Logger.i("MainActivity","onStart","");
            }
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void onResume(){
Logger.i("MainActivity","onResume","");
            }
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            public void onPause(){
Logger.i("MainActivity","onPause","");
            }
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public void onStop(){
Logger.i("MainActivity","onStop","");
            }
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroy(){
Logger.i("MainActivity","onDestroy","");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            public void onAny(){
                Logger.i("MainActivity","onAny","");
            }
        });
    }

}
