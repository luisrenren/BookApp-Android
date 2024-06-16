package com.example.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AdminChangebook extends AppCompatActivity implements View.OnClickListener{
    SQLiteDatabase db;
    BookOpenHelper helper;

    ImageView change_bookimg;
    EditText change_booktitle,change_bookinsb,change_bookauthor,change_bookpublisher,change_bookprice,change_bookquantity,change_bookdescription;
    CheckBox change_bookis_recommend;
    Spinner change_category;

    ImageButton book_changebackbtn;
    Button book_changebtn;

    String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_changebook);

        //改变状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.t2));
            //#D5895C
        }

        //调用MyOpenHelper的构造方法
        //得到了一个帮助类
        helper = new BookOpenHelper(this);
        //获取数据库
        db = helper.getReadableDatabase();
        // 获取
        change_bookimg = findViewById(R.id.change_bookimg);
        change_booktitle = findViewById(R.id.change_booktitle);
        change_bookinsb = findViewById(R.id.change_bookinsb);
        change_category = findViewById(R.id.change_category);
        change_bookauthor = findViewById(R.id.change_bookauthor);
        change_bookpublisher = findViewById(R.id.change_bookpublisher);
        change_bookprice = findViewById(R.id.change_bookprice);
        change_bookquantity = findViewById(R.id.change_bookquantity);
        change_bookis_recommend = findViewById(R.id.change_bookis_recommend);
        change_bookdescription = findViewById(R.id.change_bookdescription);

        // 获取了跳转过来的intent
        Intent intent = getIntent();
        // 获取传递过来的 ISBN
        isbn = intent.getStringExtra("Isbn");

        book_changebackbtn = findViewById(R.id.book_changebackbtn);
        book_changebtn = findViewById(R.id.book_changebtn);

        book_changebackbtn.setOnClickListener(this);
        book_changebtn.setOnClickListener(this);

        //修改btn的颜色
        book_changebtn.setBackgroundColor(Color.parseColor("#D5895C"));

        // 查询数据库，获取书籍信息
        List<Book> bookList = helper.isbnquery(isbn);

        // 将获取的书籍信息填充到布局中
        if (bookList != null && !bookList.isEmpty()) {
            Book book = bookList.get(0); // 这里假设查询结果只有一条数据
            populateLayout(book);
        }
    }

    private void populateLayout(Book book) {

        // 将书籍信息设置到对应的控件中
        change_bookimg.setImageBitmap(BitmapFactory.decodeByteArray(book.getBookimage(), 0, book.getBookimage().length));
        change_booktitle.setText(book.getTitle());
        change_bookinsb.setText(book.getIsbn());

        // 设置 Spinner 的选中项
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        change_category.setAdapter(adapter);
        int position = adapter.getPosition(book.getCategory());
        change_category.setSelection(position);

        change_bookauthor.setText(book.getAuthor());
        change_bookpublisher.setText(book.getPublisher());
        change_bookprice.setText(book.getPrice());
        change_bookquantity.setText(String.valueOf(book.getQuantity()));

        // 设置 CheckBox 的选中状态
        change_bookis_recommend.setChecked(book.isIs_recommend());

        change_bookdescription.setText(book.getDescription());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.book_changebackbtn:
                //返回管理员页面
                finish();
                break;
            case R.id.book_changebtn:
                if(change_bookinsb.getText().toString().equals("") || change_booktitle.getText().toString().equals("") ||
                    change_bookauthor.getText().toString().equals("") || change_bookpublisher.getText().toString().equals("") ||
                    change_bookprice.getText().toString().equals("") || change_bookquantity.getText().toString().equals("") ||
                    change_bookdescription.getText().toString().equals("")){
                    Toasttext("图书信息不可为空！");
                }else{
                    if(helper.admin_change_bookdata(isbn, change_bookinsb.getText().toString(), change_booktitle.getText().toString(),
                            change_category.getSelectedItem().toString(), change_bookauthor.getText().toString(),
                            change_bookpublisher.getText().toString(), change_bookprice.getText().toString(),
                            change_bookquantity.getText().toString(), change_bookis_recommend.isChecked(),
                            change_bookdescription.getText().toString())){
                        //跳转
                        Intent intent_succeedback = new Intent();
                        intent_succeedback.setClass(this,AdminActivity.class);
                        startActivity(intent_succeedback);
                    }else{
                        Log.i("test","图书信息修改失败");
                    }
                }
                break;
        }
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        toast.show();
    }
}