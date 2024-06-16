package com.example.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginB extends Fragment {
    SQLiteDatabase db;
    MyOpenHelper helper;

    EditText phoneedit, passwordedit1, passwordedit2;
    Button registerbtn;

    //这一坨是监听的,注册成功就跳转到LoginA
//    public interface OnRegistrationSuccessListener {
//        void onRegistrationSuccess();
//    }
//
//    private OnRegistrationSuccessListener registrationSuccessListener;
//
//    public void setOnRegistrationSuccessListener(OnRegistrationSuccessListener listener) {
//        this.registrationSuccessListener = listener;
//    }
    //——————————

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_b,null);

        //调用MyOpenHelper的构造方法
        //得到了一个帮助类
        helper = new MyOpenHelper(getContext());
        //获取数据库
        db = helper.getReadableDatabase();

        // 获取id
        phoneedit = view.findViewById(R.id.phoneedit);
        passwordedit1 = view.findViewById(R.id.passwordedit1);
        passwordedit2 = view.findViewById(R.id.passwordedit2);
        registerbtn = view.findViewById(R.id.registerbtn);

        //给按钮换色
        registerbtn.setBackgroundColor(Color.parseColor("#3D68C2"));

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("test","registerbtn");
                //trim()是去除输入框的空白
                if(phoneedit.getText().toString().equals("")){
                    Toasttext("手机号不能为空！");
                }else if(passwordedit1.getText().toString().equals("")) {
                    Toasttext("密码不能为空！");
                } else if(passwordedit2.getText().toString().equals("")) {
                    Toasttext("请再次输入密码！");
                }else if(phoneedit.getText().toString().trim().length()!=11){
                    Toasttext("手机号位数不满足11位");
                }else if(helper.phonedata(phoneedit.getText().toString())){
                    Toasttext("该手机号已被注册");
                }else if(passwordedit1.getText().toString().trim().length()<6){
                    Toasttext("密码不能小于6位数！");
                }else if(passwordedit1.getText().toString().equals(passwordedit2.getText().toString())==false){
                    Toasttext("两次输入的密码不同！");
                }else{
                    if(helper.registerdata(phoneedit.getText().toString(),
                            passwordedit1.getText().toString())){
                        Log.i("test","注册信息插入成功！");
//                        if (registrationSuccessListener != null) {
//                            registrationSuccessListener.onRegistrationSuccess();
//                        }
                        Intent intent_inputname = new Intent();
                        intent_inputname.setClass(getContext(),InputnameActivity.class);
                        intent_inputname.putExtra("PhoneEdit",phoneedit.getText().toString());
                        //进行跳转
                        startActivity(intent_inputname);
                    }
                }
            }
        });
        return view;
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
}