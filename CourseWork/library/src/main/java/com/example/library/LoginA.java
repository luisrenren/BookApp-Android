package com.example.library;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginA extends Fragment {
    SQLiteDatabase db;
    MyOpenHelper helper;

    TextView adminlogin;
    EditText usernameedit,passwordedit;
    Button loginbtn;
    CheckBox auto_login;

    SharedPreferences sp;
    Boolean isAuto;
    String AutoUsername,AutoPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_a,null);

        //调用MyOpenHelper的构造方法
        //得到了一个帮助类
        helper = new MyOpenHelper(getContext());
        //获取数据库
        db = helper.getReadableDatabase();

        usernameedit = view.findViewById(R.id.usernameedit);
        passwordedit = view.findViewById(R.id.passwordedit);
        adminlogin = view.findViewById(R.id.adminlogin);
        loginbtn = view.findViewById(R.id.loginbtn);
        auto_login = view.findViewById(R.id.auto_login);

        sp = requireContext().getSharedPreferences("AutoLogin",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //一开始的时候给isAuto赋值false，不自动登录
        isAuto = sp.getBoolean("isAuto",false);

        //如果checkbox没勾选，isAuto会一直为false
        //如果isAuto == true
        if(isAuto){
            //自动登录
            Intent intent_home = new Intent();
            intent_home.setClass(getContext(),MainActivity.class);
            intent_home.putExtra("UserName",sp.getString("username","not found name is sp"));
            startActivity(intent_home);
        }

        //给按钮换色
        loginbtn.setBackgroundColor(Color.parseColor("#3D68C2"));

        //点击登录的按钮
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("test","loginbtn");
                if(usernameedit.getText().toString().equals("")||passwordedit.getText().toString().equals("")){
                    Toasttext("用户名和密码不可为空！");
                }else if(helper.logindata(usernameedit.getText().toString(),passwordedit.getText().toString())){
                    //如果勾选自动登录
                    if(auto_login.isChecked()){
                        //把isAuto改成true 并把用户名和密码保存到sp里
                        editor.putBoolean("isAuto",true);
                        editor.putString("username",usernameedit.getText().toString());
                        editor.putString("password",passwordedit.getText().toString());
                        editor.commit();
                    }else{
                        //把isAuto改成false
                        editor.putBoolean("isAuto",false);
                        editor.commit();
                    }

                    //用户名密码正确，跳转主页
                    Intent intent_home = new Intent();
                    intent_home.setClass(getContext(),MainActivity.class);
                    intent_home.putExtra("UserName",usernameedit.getText().toString());
                    startActivity(intent_home);
                }else{
                    Toasttext("用户名或密码有误");
                }
            }
        });

        //跳转到管理员登录页面
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_adminlogin = new Intent();
                intent_adminlogin.setClass(getContext(),AdminloginActivity.class);
                startActivity(intent_adminlogin);
            }
        });

        return view;
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
}