package com.example.library;

import static android.content.Context.MODE_PRIVATE;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;

public class UserSelf extends Fragment {
    SharedPreferences sp;
    Toolbar toolbar;

    SQLiteDatabase db;
    MyOpenHelper helper;

    ImageView user_avatar;
    TextView user_username,user_gender,user_phone,user_birthday;
    View user_gender_box,user_birthday_box,user_phone_box;

    private String usernameData;

    AlertDialog gender_dialog,phone_dialog;
    DatePickerDialog birthday_dialog;

    EditText editphone;

    Boolean isAuto;

    int choice = 0;

    Bitmap bitmap;
    //协议 用到的协议  这一条很长
    ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            //Uri 数据的Uri地址
            //内容获取者
            ContentResolver cr = getContext().getContentResolver();
            try {
                //通过输入流来生成一个bitmap
                bitmap = BitmapFactory.decodeStream(cr.openInputStream(result));
                //设置图片
                user_avatar.setImageBitmap(bitmap);
                if (helper.change_avatar(usernameData,bitmap)){
                    Toasttext("头像修改成功！");
                }else {
                    Toasttext("头像修改失败");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_self,null);

        //调用MyOpenHelper的构造方法
        //得到了一个帮助类
        helper = new MyOpenHelper(getContext());
        //获取数据库
        db = helper.getReadableDatabase();

        //获取
        toolbar = view.findViewById(R.id.usertop7_toolbar);
        //就是把参数toolbar作为actionbar使用
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("");

        sp = requireContext().getSharedPreferences("AutoLogin",MODE_PRIVATE);
        //用isAuto获取里面的是true还是false
        isAuto = sp.getBoolean("isAuto",false);

        user_username = view.findViewById(R.id.user_username);
        user_gender = view.findViewById(R.id.user_gender);
        user_phone = view.findViewById(R.id.user_phone);
        user_birthday = view.findViewById(R.id.user_birthday);
        user_avatar = view.findViewById(R.id.user_avatar);
        user_gender_box = view.findViewById(R.id.user_gender_box);
        user_birthday_box = view.findViewById(R.id.user_birthday_box);
        user_phone_box = view.findViewById(R.id.user_phone_box);

        //读取数据，然后设置数据
        getusernameData();

        //点击设置性别
        user_gender_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] genderitems = {"男","女","保密"};
                gender_dialog = new AlertDialog.Builder(getContext())
                        .setTitle("设置性别")
                        .setSingleChoiceItems(genderitems, choice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                choice=i;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(helper.change_gender(usernameData,genderitems[choice])){
                                    user_gender.setText(genderitems[choice]);
                                    Toasttext("性别设置成功！");
                                }else{
                                    Toasttext("设置失败");
                                }
                            }
                        })
                        .show();
            }
        });

        //点击设置生日
        user_birthday_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                birthday_dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //年 月 日
                        if(helper.change_birthday(usernameData, i + "-" + (i1+1) + "-" +i2 )){
                            user_birthday.setText(i + "-" + (i1+1) + "-" +i2);
                            Toasttext("生日设置成功！");
                        }else{
                            Toasttext("设置失败");
                        }

                    }
                },year,month,day);

                birthday_dialog.show();
            }
        });

        //点击设置手机号
        user_phone_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_dialog = new AlertDialog.Builder(getContext())
                        .setTitle("请重置手机号")
                        .setView(editphone = new EditText(getContext()))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(editphone.getText().toString().trim().length()!=11){
                                    Toasttext("手机号不满足11位");
                                }
                                //查询是否是原来的手机号
                                else if(editphone.getText().toString().equals(user_phone.getText().toString())){
                                    Toasttext("您输入的手机号和原先手机号相同");
                                }
                                //查询输入的手机号是否重复
                                else if(helper.phonedata(editphone.getText().toString())){
                                    Toasttext("该手机号已被注册");
                                }else{
                                    //更换手机号
                                    if (helper.change_phone(usernameData,editphone.getText().toString())){
                                        user_phone.setText(editphone.getText().toString());
                                        Toasttext("手机号重置成功！");
                                    }else{
                                        Toasttext("重置失败");
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

            }
        });

        user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开系统相册
                //   /*表示要查找所有img图片
                launcher.launch("image/*");
            }
        });

        return view;
    }

    //设置一个静态方法来从MainActivity获取用户名数据
    public static UserSelf newInstance(String username){
        UserSelf fragment = new UserSelf();
        Bundle args = new Bundle();
        args.putString("username_self",username);
        fragment.setArguments(args);
        return fragment;
    }

    // 在这里获取数据的方法
    private void getusernameData() {
        if (getArguments() != null) {
            //获取username
            usernameData = getArguments().getString("username_self");
            //查询username
            List<User> userList = helper.username_query(usernameData);
            if(userList != null && ! userList.isEmpty()){
                //获取第一条数据
                User user  = userList.get(0);
                populateLayout(user);
            }
        }
    }

    private void populateLayout(User user) {
        // 将用户信息设置到对应的控件中
        // 将书籍信息设置到对应的控件中
        if(user.getUserimage()!=null){
            user_avatar.setImageBitmap(BitmapFactory.decodeByteArray(user.getUserimage(), 0, user.getUserimage().length));
        }
        user_username.setText(user.getUsername());
        if (user.getGender() == null){ user_gender.setText("待设置");
        }else{ user_gender.setText(user.getGender()); }
        user_phone.setText(user.getPhone());
        if (user.getBirthday() == null){ user_birthday.setText("待设置");
        }else{ user_birthday.setText(user.getBirthday()); }
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
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
                //退出账号后把自动登录关闭
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isAuto",false);
                editor.commit();
                //退出跳转到登陆页面
                Intent intent_login = new Intent();
                intent_login.setClass(getContext(),LoginActivity.class);
                startActivity(intent_login);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}