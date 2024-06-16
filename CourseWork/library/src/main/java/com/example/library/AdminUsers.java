package com.example.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdminUsers extends Fragment {
    SQLiteDatabase db;
    MyOpenHelper helper;

    ListView user_listview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_users,null);

        //调用MyOpenHelper的构造方法
        //得到了一个帮助类
        helper = new MyOpenHelper(getContext());
        //获取数据库
        db = helper.getReadableDatabase();
        //获取列表装载区域
        user_listview = view.findViewById(R.id.user_listview);

        //一上来就查询列表
        user_displayall();

        //短按单条用户触发修改判断
        user_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater factory = LayoutInflater.from(getContext());
                View changeuser_view = factory.inflate(R.layout.changeuser_view,null);
                //当前获取的用户信息
                TextView tvusername = view.findViewById(R.id.tvname);
                TextView tvphone = view.findViewById(R.id.tvphone);
                TextView tvpassword= view.findViewById(R.id.tvpassword);
                TextView tvgender = view.findViewById(R.id.tvgender);
                TextView tvbirthday = view.findViewById(R.id.tvbirthday);

                //把当前获取的用户信息放到各个输入框上
                EditText changeusername,changephone,changepassword,changegender,changebirthday;
                changeusername = changeuser_view.findViewById(R.id.changeusername);
                changephone = changeuser_view.findViewById(R.id.changephone);
                changepassword = changeuser_view.findViewById(R.id.changepassword);
                changegender = changeuser_view.findViewById(R.id.changegender);
                changebirthday = changeuser_view.findViewById(R.id.changebirthday);
                changeusername.setText(tvusername.getText().toString());
                changephone.setText(tvphone.getText().toString());
                changepassword.setText(tvpassword.getText().toString());
                changegender.setText(tvgender.getText().toString());
                changebirthday.setText(tvbirthday.getText().toString());

                //其中手机号不可修改
                changephone.setEnabled(false);

                AlertDialog change_dlg = new AlertDialog.Builder(getContext())
                        .setTitle("请更改用户信息")
                        .setView(changeuser_view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(changeusername.getText().toString().equals("") || changepassword.getText().toString().equals("") ||
                                        changegender.getText().toString().equals("") || changebirthday.getText().toString().equals("")){
                                    Toasttext("信息不可为空！");
                                }else{
                                    if(helper.admin_change_userdata(changephone.getText().toString(),
                                            changeusername.getText().toString(),
                                            changepassword.getText().toString(),
                                            changegender.getText().toString(),
                                            changebirthday.getText().toString() )){
                                        Toasttext("修改成功！");
                                        //更新页面
                                        user_displayall();
                                    }else{
                                        Toasttext("修改失败！");
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        //长按单条用户触发删除判断
        user_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //靠下标i删除用户不靠谱,因为sql数据库不会自动调节id呃
                //我这里用phone作为唯一标识
                TextView tvphone = view.findViewById(R.id.tvphone);
                String phone = tvphone.getText().toString();

                AlertDialog delete_dlg = new AlertDialog.Builder(getContext())
                        .setTitle("是否删除该用户？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(helper.delete_userdata(phone)){
                                    Toasttext("删除用户成功！");
                                    //更新页面
                                    user_displayall();
                                }else{Toasttext("删除用户失败！");}
                            }
                        })
                        .setNegativeButton("否",null)
                        .show();
                return true;
            }
        });

        return view;
    }

    //查询user列表
    private void user_displayall(){
        List<User> mlist = new ArrayList<User>();
        mlist = helper.user_query();

        MyuserAdapter myuserAdapter = new MyuserAdapter(getContext(),mlist);
        user_listview.setAdapter(myuserAdapter);
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
}