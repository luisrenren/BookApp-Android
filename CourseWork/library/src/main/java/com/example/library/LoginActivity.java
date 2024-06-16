package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager2 viewPager2;
    List<Fragment> loginlistfragment = new ArrayList<Fragment>();
    Button login,register,buttonchoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t1));
            //#041043
        }

        //绑定控件
        viewPager2 = findViewById(R.id.viewpager2);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        //填充数据, 把登录和注册fragment放到loginlistfragment列表里
        LoginA loginA = new LoginA();
        LoginB loginB = new LoginB();
//        loginB.setOnRegistrationSuccessListener(this);
        loginlistfragment.add(loginA);
        loginlistfragment.add(loginB);
        //创建adapter
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this,loginlistfragment);
        //设置adapter
        viewPager2.setAdapter(fragmentAdapter);

        //初始化使让login变蓝色
        login.setSelected(true);
        login.setBackgroundColor(Color.parseColor("#3D68C2"));
        login.setTextColor(Color.parseColor("#FFFFFF"));
        register.setBackgroundColor(Color.parseColor("#FFFFFF"));
        register.setTextColor(Color.parseColor("#3D68C2"));
        buttonchoice=login;

        //滑动页面，按钮也跟着变化
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        changebottomitem(login);
                        break;
                    case 1:
                        changebottomitem(register);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    public void onClick(View view) {
        //设置点击按钮，显示当前的item
        switch (view.getId()){
            case R.id.login:
                //设置点击显示当前的item
                viewPager2.setCurrentItem(0);
                changebottomitem(login);
                break;
            case R.id.register:
                //设置点击显示当前的item
                viewPager2.setCurrentItem(1);
                changebottomitem(register);
                break;
        }
    }

    //改变按钮的方法
    private void changebottomitem(Button button) {
        //先关闭上一个
        buttonchoice.setSelected(false);
        buttonchoice.setBackgroundColor(Color.parseColor("#FFFFFF"));
        buttonchoice.setTextColor(Color.parseColor("#3D68C2"));
        switch (button.getId()){
            case R.id.login:
                buttonchoice = login;
                break;
            case R.id.register:
                buttonchoice = register;
                break;
        }
        //打开当前的
        buttonchoice.setBackgroundColor(Color.parseColor("#3D68C2"));
        buttonchoice.setTextColor(Color.parseColor("#FFFFFF"));
        buttonchoice.setSelected(true);
    }

//    @Override
//    public void onRegistrationSuccess() {
//        // 注册成功后切换到LoginA页面
//        viewPager2.setCurrentItem(0);
//    }
}