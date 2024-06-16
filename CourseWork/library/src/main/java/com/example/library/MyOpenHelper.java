package com.example.library;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyOpenHelper extends SQLiteOpenHelper {
    //id的这个_是必须的一个字段，整形自增，主键，  cusoradapter有要求_id列
    public String US_ID = "_id";
    public String US_Name = "name";
    public String US_Password = "password";
    public String US_Phone = "phone";
    public String US_Gender = "gender";
    public String US_Birthday = "birthday";
    public String US_Userimage = "userimage";
    public String TABLE_NAME = "user";

    //定义一个db
    private SQLiteDatabase db;

    //这里的每一个空格都必不可少↓
    private String CREAT_TABLE_USER = "create table "+TABLE_NAME+"("+
            US_ID+" integer primary key autoincrement,"+US_Name+","+US_Password+","+US_Phone+","+US_Gender+","+US_Birthday+","+US_Userimage+")";

    //构造方法   上下文、数据库的名字、工厂null、版本号
    public MyOpenHelper(@Nullable Context context, int version) {
        super(context, "user.db",null, version);
    }

    //第二个构造方法
    public MyOpenHelper(@Nullable Context context) {
        super(context, "user.db",null, 1);
        //生成数据库，this指MyOpenHelper
        db = this.getReadableDatabase();
    }

    //只被调用一次，第一次调用，创建表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //这个方法就是执行SQL语句
        sqLiteDatabase.execSQL(CREAT_TABLE_USER);
    }

    //版本更新
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("test","onupgrade !");
    }

    //注册时的插入，只插入手机号和密码
    public boolean registerdata(String phone,String password){
        //表名、插入数据为null的列的列名，contentvalues的类型
        ContentValues values = new ContentValues();
        values.put(US_Phone,phone);
        values.put(US_Password,password);
        Long lineid = db.insert(TABLE_NAME,null,values);
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //注册成功后插入用户名
    public boolean inputnamedata(String phone, String name){
        ContentValues values = new ContentValues();
        values.put(US_Name,name);
        int lineid = db.update(TABLE_NAME,values,US_Phone + "=?", new String[]{phone});
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //登录判定
    public boolean logindata(String name,String password){
        String[] columns = {US_Name,US_Password};
        String selection = US_Name + "=? AND " + US_Password + "=?";
        String[] selectionArgs = {name,password};

        Cursor cursor = db.query(TABLE_NAME,
                columns, //查询的列
                selection, //查询语句（where语句） 如：age>?
                selectionArgs, //where语句对应的值
                null,
                null,
                null);

        //判断是否存在匹配的用户 判断cursor查询出来的结果不为空，且数量>0
        boolean loginOk = cursor!=null && cursor.getCount()>0;

        //关闭Cursor,释放资源
        if (cursor!=null){ cursor.close();}

        return loginOk;
    }

    //查询用户名是否重复
    public boolean usernamedata(String name){
        Cursor cursor = db.query(TABLE_NAME,
                null, //查询的列
                US_Name+"=?", //查询语句（where语句） 如：age>?
                new String[]{name}, //where语句对应的值
                null,
                null,
                null);

        //判断是否存在匹配的用户 判断cursor查询出来的结果不为空，且数量>0
        boolean loginOk = cursor!=null && cursor.getCount()>0;

        //关闭Cursor,释放资源
        if (cursor!=null){ cursor.close();}
        return loginOk;
    }

    //查询手机号是否重复
    public boolean phonedata(String phone){
        Cursor cursor = db.query(TABLE_NAME,
                null, //查询的列
                US_Phone+"=?", //查询语句（where语句） 如：age>?
                new String[]{phone}, //where语句对应的值
                null,
                null,
                null);

        //判断是否存在匹配的用户 判断cursor查询出来的结果不为空，且数量>0
        boolean loginOk = cursor!=null && cursor.getCount()>0;

        //关闭Cursor,释放资源
        if (cursor!=null){ cursor.close();}
        return loginOk;
    }

    //查询user列表
    public List<User> user_query(){
        List<User> mlist = new ArrayList<User>();
        //查询语句
        Cursor cursor = db.query(TABLE_NAME,
                null, //查询所有的列
                null, //where语句   如：age>?
                null, //where语句对应的值，是一个string类型的数组 如：new String[]{"19"}
                null, //groupby
                null, //having
                null //orderby
        );
        //moveToNext移动到第一条
        //将数据库里的记录存到list里
        while (cursor.moveToNext()){
            @SuppressLint("Range") byte[] imagebyte = cursor.getBlob(cursor.getColumnIndex(US_Userimage));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(US_Name));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(US_Password));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(US_Phone));
            @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(US_Gender));
            @SuppressLint("Range") String birthday = cursor.getString(cursor.getColumnIndex(US_Birthday));
            mlist.add(new User(name,password,phone,gender,birthday,imagebyte));
        }
        return mlist;
    }

    //删除用户判定
    public boolean delete_userdata(String phone){
        long delete_ok = db.delete(TABLE_NAME,US_Phone+"=?",new String[]{phone});
        //返回-1表明插入失败,不返回-1就是成功
            if(delete_ok!=-1){
                return true;
            }else{
                return false;
            }
    }

    //管理员修改用户信息
    public boolean admin_change_userdata(String phone, String name,String password,String gender,String birthday){
        ContentValues values = new ContentValues();
        values.put(US_Name,name);
        values.put(US_Password,password);
        values.put(US_Gender,gender);
        values.put(US_Birthday,birthday);
        int lineid = db.update(TABLE_NAME,values,US_Phone + "=?", new String[]{phone});
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //查询user列表,但是根据username查询的
    public List<User> username_query(String username){
        List<User> mlist = new ArrayList<User>();
        //查询语句
        Cursor cursor = db.query(TABLE_NAME,
                null, //查询所有的列
                US_Name + "=?", //where语句   如：age>?
                new String[]{username}, //where语句对应的值，是一个string类型的数组 如：new String[]{"19"}
                null, //groupby
                null, //having
                null //orderby
        );
        //moveToNext移动到第一条
        //将数据库里的记录存到list里
        while (cursor.moveToNext()){
            @SuppressLint("Range") byte[] imagebyte = cursor.getBlob(cursor.getColumnIndex(US_Userimage));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(US_Name));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(US_Password));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(US_Phone));
            @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(US_Gender));
            @SuppressLint("Range") String birthday = cursor.getString(cursor.getColumnIndex(US_Birthday));
            mlist.add(new User(name,password,phone,gender,birthday,imagebyte));
        }
        return mlist;
    }

    //修改性别
    public boolean change_gender(String username,String gender){
        ContentValues values = new ContentValues();
        values.put(US_Gender,gender);

        int lineid = db.update(TABLE_NAME,values,US_Name + "=?", new String[]{username});
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //修改生日
    public boolean change_birthday(String username,String birthday){
        ContentValues values = new ContentValues();
        values.put(US_Birthday,birthday);

        int lineid = db.update(TABLE_NAME,values,US_Name + "=?", new String[]{username});
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //修改手机号
    public boolean change_phone(String username,String phone){
        ContentValues values = new ContentValues();
        values.put(US_Phone,phone);

        int lineid = db.update(TABLE_NAME,values,US_Name + "=?", new String[]{username});
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //修改头像
    public boolean change_avatar(String username, Bitmap bitmap){
        ContentValues values = new ContentValues();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        values.put(US_Userimage,os.toByteArray());

        int lineid = db.update(TABLE_NAME,values,US_Name + "=?", new String[]{username});
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }
}
