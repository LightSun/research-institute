package com.heaven.android.lua.lds.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTest1(View view){
        LuaDesigner.testBase();
    }

    public void onClickTestSetjmp(View view){
        LuaDesigner.testSetJmp();
    }
    public void onClickTestSetjmp2(View view){
        LuaDesigner.testSetJmp2();
    }
}
