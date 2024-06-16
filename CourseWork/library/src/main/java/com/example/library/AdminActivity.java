package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager2 viewPager2;
    List<Fragment> adminlistfragment = new ArrayList<Fragment>();
    ImageButton books_1,users_2,self_3,imagechoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t2));
            //#D5895C
        }

        //绑定控件
        viewPager2 = findViewById(R.id.viewpager2_admin);
        books_1 = findViewById(R.id.books_1);
        users_2 = findViewById(R.id.users_2);
        self_3 = findViewById(R.id.self_3);
        books_1.setOnClickListener(this);
        users_2.setOnClickListener(this);
        self_3.setOnClickListener(this);
        //填充数据
        AdminBooks adminBooks = new AdminBooks();
        AdminUsers adminUsers = new AdminUsers();
        AdminSelf adminSelf = new AdminSelf();
        adminlistfragment.add(adminBooks);
        adminlistfragment.add(adminUsers);
        adminlistfragment.add(adminSelf);
        //创建adapter
        FragmentAdapter fragmentAdapter1 = new FragmentAdapter(this,adminlistfragment);
        //设置adapter
        viewPager2.setAdapter(fragmentAdapter1);

        //初始化时把image1变深色
        books_1.setSelected(true);
        imagechoice=books_1;

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
                        changebottomitem(books_1);
                        break;
                    case 1:
                        changebottomitem(users_2);
                        break;
                    case 2:
                        changebottomitem(self_3);
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
        switch (view.getId()){
            case R.id.books_1:
                //设置点击显示当前的item
                viewPager2.setCurrentItem(0);
                changebottomitem(books_1);
                break;
            case R.id.users_2:
                viewPager2.setCurrentItem(1);
                changebottomitem(users_2);
                break;
            case R.id.self_3:
                viewPager2.setCurrentItem(2);
                changebottomitem(self_3);
                break;
        }
    }
    //改变按钮的方法
    private void changebottomitem(ImageButton image) {
        //先关闭上一个
        imagechoice.setSelected(false);
        switch (image.getId()){
            case R.id.books_1:
                imagechoice = books_1;
                break;
            case R.id.users_2:
                imagechoice = users_2;
                break;
            case R.id.self_3:
                imagechoice = self_3;
                break;
        }
        //打开当前的
        imagechoice.setSelected(true);
    }
}