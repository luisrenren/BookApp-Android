package com.example.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserHomepage extends Fragment {
    ViewPager2 carousel_viewpager2;
    List<Fragment> carousel_listfragment = new ArrayList<Fragment>();
    Handler handler;

    SQLiteDatabase db;
    BookOpenHelper rec_helper;

    ListView recommend_listview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_homepage,null);

        //绑定
        carousel_viewpager2 = view.findViewById(R.id.carousel_viewpager2);
        //填充数据
        HaibaoA haibaoA = new HaibaoA();
        HaibaoB haibaoB = new HaibaoB();
        HaibaoC haibaoC = new HaibaoC();
        carousel_listfragment.add(haibaoA);
        carousel_listfragment.add(haibaoB);
        carousel_listfragment.add(haibaoC);

        //幻灯片
        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = carousel_viewpager2.getCurrentItem();
                int totalItems = carousel_viewpager2.getAdapter().getItemCount();
                int nextItem = (currentItem + 1) % totalItems;
                carousel_viewpager2.setCurrentItem(nextItem,true);
                handler.postDelayed(this,3000);//切换间隔 3s
            }
        };
        handler.postDelayed(runnable,3000); //初始延迟，3s后开始

        //创建adapter
        FragmentAdapter carousel_adapter = new FragmentAdapter(getActivity(),carousel_listfragment);
        //设置adapter
        carousel_viewpager2.setAdapter(carousel_adapter);

        carousel_viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 如果用户手动切换了页面，取消自动切换
                handler.removeCallbacksAndMessages(null);

                // 重新添加延迟任务，确保手动切换后仍然自动播放
                handler.postDelayed(runnable, 3000);
            }
        });

        //调用BookOpenHelper的构造方法
        //得到了一个帮助类
        rec_helper = new BookOpenHelper(getContext());
        //获取数据库
        db = rec_helper.getReadableDatabase();
        //获取列表装载区域
        recommend_listview = view.findViewById(R.id.recommend_listview);
        //查询,装载
        recommend_displayall();

        //点击列表跳转
        recommend_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取当前书籍的isbn
                TextView recisbn = view.findViewById(R.id.recisbn);

                Intent intent_showbook = new Intent();
                intent_showbook.setClass(getContext(),ShowBook.class);
                //把当前书籍的isbn传到修改页
                intent_showbook.putExtra("Isbn2",recisbn.getText().toString());
                startActivity(intent_showbook);
            }
        });

        return view;
    }

    //查询推荐列表
    private void recommend_displayall(){
        List<Book> mlist = new ArrayList<Book>();
        mlist = rec_helper.rec_bookquery();

        RecommendbookAdapter recommendbookAdapter = new RecommendbookAdapter(getContext(),mlist);
        recommend_listview.setAdapter(recommendbookAdapter);
    }

}