package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    SharedPreferences sp;
    boolean isfirst;

    Button welcomebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t3));
            //#3D68C2
        }

        //获取
        welcomebtn = findViewById(R.id.welcomebtn);

        //给按钮换色
        welcomebtn.setBackgroundColor(Color.parseColor("#3D68C2"));

        sp = getSharedPreferences("Welcome",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        isfirst = sp.getBoolean("isFirst",true);

        if(isfirst){
            //如果为真，说明是第一次运行
            welcomebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //把isFirst改成false
                    editor.putBoolean("isFirst",false);
                    editor.commit();
                    //跳转进入首页
                    tomainactivity();
                }
            });

        }else{
            //如果为假，说明不是第一次运行，直接进入首页
            tomainactivity();
        }
    }

    private void tomainactivity() {
        Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}