package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager2 viewPager2;
    List<Fragment> userlistfragment = new ArrayList<Fragment>();
    ImageButton homepage_1,search_2,userself_3,imagechoice;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t3));
            //#3D68C2
        }

        //获取了跳转过来的intent
        Intent intent = getIntent();
        //获取传递过来的isbn
        username = intent.getStringExtra("UserName");

        //绑定控件
        viewPager2 = findViewById(R.id.viewpager2_user);
        homepage_1 = findViewById(R.id.homepage_1);
        search_2 = findViewById(R.id.search_2);
        userself_3 = findViewById(R.id.userself_3);
        homepage_1.setOnClickListener(this);
        search_2.setOnClickListener(this);
        userself_3.setOnClickListener(this);
        //填充数据
        UserHomepage userHomepage = new UserHomepage();
        UserBooks userBooks = new UserBooks();
        UserSelf userSelf = UserSelf.newInstance(username);
        userlistfragment.add(userHomepage);
        userlistfragment.add(userBooks);
        userlistfragment.add(userSelf);
        //创建adapter
        FragmentAdapter fragmentAdapter2 = new FragmentAdapter(this,userlistfragment);
        //设置adapter
        viewPager2.setAdapter(fragmentAdapter2);

        //初始化时把image1变深色
        homepage_1.setSelected(true);
        imagechoice=homepage_1;

        //禁止滑屏切换
        viewPager2.setUserInputEnabled(false);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                switch (position){
//                    case 0:
//                        changebottomitem(homepage_1);
//                        break;
//                    case 1:
//                        changebottomitem(search_2);
//                        break;
//                    case 2:
//                        changebottomitem(userself_3);
//                        break;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.homepage_1:
                //设置点击显示当前的item
                viewPager2.setCurrentItem(0);
                changebottomitem(homepage_1);
                break;
            case R.id.search_2:
                viewPager2.setCurrentItem(1);
                changebottomitem(search_2);
                break;
            case R.id.userself_3:
                viewPager2.setCurrentItem(2);
                changebottomitem(userself_3);
                break;
        }
    }
    //改变按钮的方法
    private void changebottomitem(ImageButton image) {
        //先关闭上一个
        imagechoice.setSelected(false);
        switch (image.getId()){
            case R.id.homepage_1:
                imagechoice = homepage_1;
                break;
            case R.id.search_2:
                imagechoice = search_2;
                break;
            case R.id.userself_3:
                imagechoice = userself_3;
                break;
        }
        //打开当前的
        imagechoice.setSelected(true);
    }
}