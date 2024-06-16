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

public class BookOpenHelper extends SQLiteOpenHelper {
    public String BK_ID = "_id";
    public String BK_Isbn = "isbn";
    public String BK_Title = "title";
    public String BK_Category = "category";
    public String BK_Author = "author";
    public String BK_Publisher = "publisher";
    public String BK_Price = "price";
    public String BK_Quantity = "quantity";
    public String BK_Is_recommend = "is_recommend";
    public String BK_Description = "description";
    public String BK_Bookimage = "bookimage";
    public String TABLE_NAME = "book";

    //定义一个db
    private SQLiteDatabase db;

    //这里的每一个空格都必不可少↓
    private String CREAT_TABLE_BOOK = "create table "+TABLE_NAME+"("+
            BK_ID+" integer primary key autoincrement,"+BK_Isbn+","+BK_Title+","+BK_Category+","+
            BK_Author+","+BK_Publisher+","+BK_Price+","+BK_Quantity+","+BK_Is_recommend+","+BK_Description+","+BK_Bookimage+")";

    //构造方法   上下文、数据库的名字、工厂null、版本号
    public BookOpenHelper(@Nullable Context context, int version) {
        super(context, "book.db",null, version);
        db = this.getReadableDatabase();
    }

    //第二个构造方法
    public BookOpenHelper(@Nullable Context context) {
        super(context, "book.db",null, 1);
        //生成数据库，this指MyOpenHelper
        db = this.getReadableDatabase();
    }

    //只被调用一次，第一次调用，创建表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //这个方法就是执行SQL语句
        sqLiteDatabase.execSQL(CREAT_TABLE_BOOK);
    }

    //版本更新
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("test","onupgrade !");
    }

    //插入图书信息
    public boolean insertbookdata(Bitmap bitmap,String isbn, String title,String category,String author,
                                String publisher,String price,String quantity,boolean is_recommend,String description){
        //表名、插入数据为null的列的列名，contentvalues的类型
        ContentValues values = new ContentValues();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
        values.put(BK_Bookimage,os.toByteArray());
        values.put(BK_Isbn,isbn);
        values.put(BK_Title,title);
        values.put(BK_Category,category);
        values.put(BK_Author,author);
        values.put(BK_Publisher,publisher);
        values.put(BK_Price,price);
        values.put(BK_Quantity,quantity);
        values.put(BK_Is_recommend,is_recommend);
        values.put(BK_Description,description);

        Long lineid = db.insert(TABLE_NAME,null,values);
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //查询列表bookquery()
    public List<Book> bookquery(){
        List<Book> mlist = new ArrayList<Book>();

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
            @SuppressLint("Range") byte[] imagebyte = cursor.getBlob(cursor.getColumnIndex(BK_Bookimage));
            @SuppressLint("Range") String isbn = cursor.getString(cursor.getColumnIndex(BK_Isbn));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(BK_Title));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(BK_Category));
            @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(BK_Author));
            @SuppressLint("Range") String publisher = cursor.getString(cursor.getColumnIndex(BK_Publisher));
            @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(BK_Price));
            @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(BK_Quantity));
            @SuppressLint("Range") boolean is_recommend = cursor.getInt(cursor.getColumnIndexOrThrow(BK_Is_recommend)) == 1;
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(BK_Description));

            mlist.add(new Book(isbn,title,category,author,publisher,price,quantity,is_recommend,description,imagebyte));
        }
        return mlist;
    }

    //删除书籍判定
    public boolean delete_bookdata(String isbn){
        long delete_ok = db.delete(TABLE_NAME,BK_Isbn+"=?",new String[]{isbn});
        //返回-1表明插入失败,不返回-1就是成功
        if(delete_ok!=-1){
            return true;
        }else{
            return false;
        }
    }

    //根据isbn查询单条数据
    public List<Book> isbnquery(String this_isbn){
        List<Book> mlist = new ArrayList<Book>();

        Cursor cursor = db.query(TABLE_NAME,
                null, //查询所有的列
                BK_Isbn+"=?", //where语句   如：age>?
                new String[]{this_isbn}, // 查询isbn
                null, //groupby
                null, //having
                null //orderby
        );

        //moveToNext移动到第一条
        //将数据库里的记录存到list里
        while (cursor.moveToNext()){
            @SuppressLint("Range") byte[] imagebyte = cursor.getBlob(cursor.getColumnIndex(BK_Bookimage));
            @SuppressLint("Range") String isbn = cursor.getString(cursor.getColumnIndex(BK_Isbn));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(BK_Title));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(BK_Category));
            @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(BK_Author));
            @SuppressLint("Range") String publisher = cursor.getString(cursor.getColumnIndex(BK_Publisher));
            @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(BK_Price));
            @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(BK_Quantity));
            @SuppressLint("Range") boolean is_recommend = cursor.getInt(cursor.getColumnIndexOrThrow(BK_Is_recommend)) == 1;
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(BK_Description));

            mlist.add(new Book(isbn,title,category,author,publisher,price,quantity,is_recommend,description,imagebyte));
        }
        return mlist;
    }

    //根据原isbn修改图书信息，isbn1是原来的，isbn2是更新的
    public boolean admin_change_bookdata(String isbn1, String isbn2, String title,String category,String author,
                                         String publisher,String price,String quantity,boolean is_recommend,String description){
        ContentValues values = new ContentValues();
        values.put(BK_Isbn,isbn2);
        values.put(BK_Title,title);
        values.put(BK_Category,category);
        values.put(BK_Author,author);
        values.put(BK_Publisher,publisher);
        values.put(BK_Price,price);
        values.put(BK_Quantity,quantity);
        values.put(BK_Is_recommend,is_recommend);
        values.put(BK_Description,description);
        int lineid = db.update(TABLE_NAME,values,BK_Isbn + "=?", new String[]{isbn1});
        //返回-1表明插入失败,不返回-1就是成功
        if(lineid!=-1){
            return true;
        }else{
            return false;
        }
    }

    //根据书名、作者、类型、isbn模糊查询
    public List<Book> searchbookquery(String search_text){
        List<Book> mlist = new ArrayList<Book>();

        // 使用LIKE操作符进行模糊查询
        // 使用OR同时在书名和作者中查询
        Cursor cursor = db.query(TABLE_NAME,
                null,
                BK_Title + " LIKE ? OR " + BK_Author + " LIKE ? OR " + BK_Isbn + " LIKE ? OR " +BK_Category + " LIKE ? " ,
                new String[]{"%" + search_text + "%", "%" + search_text + "%", "%" + search_text + "%","%" + search_text + "%"},
                null,
                null,
                null
        );

        //moveToNext移动到第一条
        //将数据库里的记录存到list里
        while (cursor.moveToNext()){
            @SuppressLint("Range") byte[] imagebyte = cursor.getBlob(cursor.getColumnIndex(BK_Bookimage));
            @SuppressLint("Range") String isbn = cursor.getString(cursor.getColumnIndex(BK_Isbn));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(BK_Title));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(BK_Category));
            @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(BK_Author));
            @SuppressLint("Range") String publisher = cursor.getString(cursor.getColumnIndex(BK_Publisher));
            @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(BK_Price));
            @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(BK_Quantity));
            @SuppressLint("Range") boolean is_recommend = cursor.getInt(cursor.getColumnIndexOrThrow(BK_Is_recommend)) == 1;
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(BK_Description));

            mlist.add(new Book(isbn,title,category,author,publisher,price,quantity,is_recommend,description,imagebyte));
        }
        return mlist;
    }

    //根据是否被推荐来查询数据，用来做推荐页
    public List<Book> rec_bookquery(){
        List<Book> mlist = new ArrayList<Book>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + BK_Is_recommend + " = 1", null);

            //moveToNext移动到第一条
        //将数据库里的记录存到list里
        while (cursor.moveToNext()){
            @SuppressLint("Range") byte[] imagebyte = cursor.getBlob(cursor.getColumnIndex(BK_Bookimage));
            @SuppressLint("Range") String isbn = cursor.getString(cursor.getColumnIndex(BK_Isbn));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(BK_Title));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(BK_Category));
            @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(BK_Author));
            @SuppressLint("Range") String publisher = cursor.getString(cursor.getColumnIndex(BK_Publisher));
            @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(BK_Price));
            @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(BK_Quantity));
            @SuppressLint("Range") boolean is_recommend = cursor.getInt(cursor.getColumnIndexOrThrow(BK_Is_recommend)) == 1;
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(BK_Description));

            mlist.add(new Book(isbn,title,category,author,publisher,price,quantity,is_recommend,description,imagebyte));
        }
        return mlist;
    }

    //根据类型查询数据
    public List<Book> categoryquery(String c){
        List<Book> mlist = new ArrayList<Book>();

        Cursor cursor = db.query(TABLE_NAME,
                null, //查询所有的列
                BK_Category+"=?", //where语句   如：age>?
                new String[]{c}, // 查询
                null, //groupby
                null, //having
                null //orderby
        );

        //moveToNext移动到第一条
        //将数据库里的记录存到list里
        while (cursor.moveToNext()){
            @SuppressLint("Range") byte[] imagebyte = cursor.getBlob(cursor.getColumnIndex(BK_Bookimage));
            @SuppressLint("Range") String isbn = cursor.getString(cursor.getColumnIndex(BK_Isbn));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(BK_Title));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(BK_Category));
            @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(BK_Author));
            @SuppressLint("Range") String publisher = cursor.getString(cursor.getColumnIndex(BK_Publisher));
            @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex(BK_Price));
            @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(BK_Quantity));
            @SuppressLint("Range") boolean is_recommend = cursor.getInt(cursor.getColumnIndexOrThrow(BK_Is_recommend)) == 1;
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(BK_Description));

            mlist.add(new Book(isbn,title,category,author,publisher,price,quantity,is_recommend,description,imagebyte));
        }
        return mlist;
    }
}
