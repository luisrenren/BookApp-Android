package com.example.library;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class AdminSelf extends Fragment {
    Toolbar toolbar;
    TextView admin_readname,admin_readusername,admin_readpassword;
    SharedPreferences sp;

    boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_self,null);

        toolbar = view.findViewById(R.id.admintop3_toolbar);
        //就是把参数toolbar作为actionbar使用
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        admin_readname = view.findViewById(R.id.admin_readname);
        admin_readusername = view.findViewById(R.id.admin_readusername);
        admin_readpassword = view.findViewById(R.id.admin_readpassword);

        // 得到sp,两个参数，模式and文件名称，文件名没有后缀
        sp = requireContext().getSharedPreferences("AdminData",MODE_PRIVATE);
        //读取，第一个参数是键，第二个是默认值（意思是没找到就默认为not found了）
        String name = sp.getString("Name","not found name");
        String username = sp.getString("UserName","not found username");
        String password = sp.getString("PassWord","not found password");
        //设置
        admin_readname.setText(name);
        admin_readusername.setText(username);
        admin_readpassword.setText("******");

        admin_readpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    flag = false;
                    admin_readpassword.setText("******");
                }else{
                    flag = true;
                    admin_readpassword.setText(password);
                }
            }
        });

        return view;
    }

    //在toolbar上设置菜单
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.admin_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //菜单各项的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.question:
                Toasttext("发送反馈申请");
                return true;
            case R.id.backtologin:
                //退出跳转到登陆页面
                Intent intent_login = new Intent();
                intent_login.setClass(getContext(),LoginActivity.class);
                startActivity(intent_login);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }

}