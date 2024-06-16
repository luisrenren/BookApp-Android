package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputnameActivity extends AppCompatActivity {
    EditText inputname;
    Button inputnamebtn;

    SQLiteDatabase db;
    MyOpenHelper helper;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputname);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t1));
            //#041043
        }

        //调用MyOpenHelper的构造方法
        //得到了一个帮助类
        helper = new MyOpenHelper(this);
        //获取数据库
        db = helper.getReadableDatabase();

        // 获取了跳转过来的intent
        intent = getIntent();
        Log.i("test",intent.getStringExtra("PhoneEdit"));

        inputname = findViewById(R.id.inputname);
        inputnamebtn = findViewById(R.id.inputnamebtn);

        //按钮换色
        inputnamebtn.setBackgroundColor(Color.parseColor("#3D68C2"));

        inputnamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputname.getText().toString().equals("")){
                    Toasttext("用户名不能为空！");
                }else if(helper.usernamedata(inputname.getText().toString())){
                    Toasttext("该用户名已被使用！");
                }else{
                    if (helper.inputnamedata(String.valueOf(intent.getStringExtra("PhoneEdit")),inputname.getText().toString())){
                        Log.i("test","用户名添加成功");
                        Intent intentbacktoLogin = new Intent();
                        intentbacktoLogin.setClass(InputnameActivity.this,LoginActivity.class);
                        startActivity(intentbacktoLogin);
                    }else{
                        Log.i("test","用户名添加失败！！！");
                    }
                }
            }
        });
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}