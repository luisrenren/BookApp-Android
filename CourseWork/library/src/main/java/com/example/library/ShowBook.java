package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ShowBook extends AppCompatActivity {
    SQLiteDatabase db;
    BookOpenHelper helper;

    ImageView show_img;
    TextView show_title,show_author,show_category,show_quantity,show_price,show_publisher,show_isbn,show_description;

    ImageButton book_backbtn;
    String isbn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t3));
        }

        //调用BookOpenHelper的构造方法
        //得到了一个帮助类
        helper = new BookOpenHelper(this);
        //获取数据库
        db = helper.getReadableDatabase();
        //获取
        show_img = findViewById(R.id.show_img);
        show_title = findViewById(R.id.show_title);
        show_author = findViewById(R.id.show_author);
        show_category = findViewById(R.id.show_category);
        show_quantity = findViewById(R.id.show_quantity);
        show_price = findViewById(R.id.show_price);
        show_publisher = findViewById(R.id.show_publisher);
        show_isbn = findViewById(R.id.show_isbn);
        show_description = findViewById(R.id.show_description);

        //获取了跳转过来的intent
        Intent intent = getIntent();
        //获取传递过来的isbn
        isbn2 = intent.getStringExtra("Isbn2");

        book_backbtn = findViewById(R.id.book_backbtn);
        //返回
        book_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //查询数据库，获取书籍信息
        List<Book> bookList = helper.isbnquery(isbn2);

        //将获取的书籍信息填充到布局中
        if(bookList != null && ! bookList.isEmpty()){
            //获取第一条数据
            Book book = bookList.get(0);
            populateLayout(book);
        }

    }

    private void populateLayout(Book book) {
        // 将书籍信息设置到对应的控件中
        show_img.setImageBitmap(BitmapFactory.decodeByteArray(book.getBookimage(), 0, book.getBookimage().length));
        show_title.setText("《"+book.getTitle()+"》");
        show_author.setText(book.getAuthor());
        show_category.setText(book.getCategory());
        show_quantity.setText(String.valueOf(book.getQuantity()));
        show_price.setText(String.valueOf(book.getPrice()));
        show_publisher.setText(book.getPublisher());
        show_isbn.setText(book.getIsbn());
        show_description.setText(book.getDescription());
    }


}