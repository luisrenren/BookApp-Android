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
import android.widget.EditText;
import android.widget.Toast;

public class AdminloginActivity extends AppCompatActivity {
    EditText adminloginusername,adminloginpassword;
    Button adminlogin_btn;

    SharedPreferences sp;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t2));
            //#D5895C
        }

        adminloginusername = findViewById(R.id.adminloginusername);
        adminloginpassword = findViewById(R.id.adminloginpassword);
        adminlogin_btn = findViewById(R.id.adminlogin_btn);

        //改变按钮的颜色
        adminlogin_btn.setBackgroundColor(Color.parseColor("#D5895C"));

        //存入sp中的学号（账号）、姓名、密码;
        sp = getSharedPreferences("AdminData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserName","210812030");
        editor.putString("Name","卢思仍");
        editor.putString("PassWord","123456");
        //提交修改
        editor.commit();
        //获取学号（账号）、密码
        username = sp.getString("UserName","");
        password = sp.getString("PassWord","");

        //点击登录按钮
        adminlogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adminloginusername.getText().toString().equals("")||adminloginpassword.getText().toString().equals("")){
                    Toasttext("账号和密码不能为空！");
                }else if(adminloginusername.getText().toString().equals(username)&&adminloginpassword.getText().toString().equals(password)){
                    Intent intent_admin = new Intent();
                    intent_admin.setClass(AdminloginActivity.this,AdminActivity.class);
                    startActivity(intent_admin);
                }else{
                    Toasttext("账号或密码有误！");
                }
            }
        });
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}