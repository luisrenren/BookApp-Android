package com.example.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserBooks extends Fragment {
    EditText user_searchbooks;
    ImageButton user_searchbtn;

    SQLiteDatabase db;
    BookOpenHelper us_sc_helper;

    TextView all,wenxue,kexue,jiaoyu,qita;

    ListView user_searchbooks_listview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_books,null);

        user_searchbooks = view.findViewById(R.id.user_searchbooks);
        user_searchbtn = view.findViewById(R.id.user_searchbtn);

        all = view.findViewById(R.id.all);
        wenxue = view.findViewById(R.id.wenxue);
        kexue = view.findViewById(R.id.kexue);
        jiaoyu = view.findViewById(R.id.jiaoyu);
        qita = view.findViewById(R.id.qita);

        //初始时把searchbtn变成true  true是搜索 false是返回
        user_searchbtn.setSelected(true);
        user_searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是搜索还是返回
                if(user_searchbtn.isSelected()){
                    //如果是搜索，判断edittext是否为空
                    if(user_searchbooks.getText().toString().equals("")){
                        Toasttext("请输入搜索的内容");
                    }else{
                        user_searchbtn.setSelected(false);
                        book_search_displayall(user_searchbooks.getText().toString());
                        //搜索完毕，输入框暂时变为不可用
                        user_searchbooks.setEnabled(false);
                    }
                }else{
                    user_searchbtn.setSelected(true);
                    //变回搜索后，把edittext清空
                    user_searchbooks.setText("");
                    //列表也变回
                    us_sc_displayall();
                    //搜索框变回可用
                    user_searchbooks.setEnabled(true);
                }
            }
        });

        //调用BookOpenHelper的构造方法
        //得到了一个帮助类
        us_sc_helper = new BookOpenHelper(getContext());
        //获取数据库
        db = us_sc_helper.getReadableDatabase();
        //获取列表装载区域
        user_searchbooks_listview = view.findViewById(R.id.user_searchbooks_listview);
        //查询,装载
        us_sc_displayall();

        //点击跳转书本详情页
        user_searchbooks_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取当前书籍的isbn
                TextView isbn = view.findViewById(R.id.us_sc_isbn);

                Intent intent_us_sc_book = new Intent();
                intent_us_sc_book.setClass(getContext(),ShowBook.class);
                //把当前书籍的isbn传到修改页
                intent_us_sc_book.putExtra("Isbn2",isbn.getText());
                startActivity(intent_us_sc_book);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                us_sc_displayall();
            }
        });

        wenxue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_displayall(wenxue.getText().toString());
            }
        });

        jiaoyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_displayall(jiaoyu.getText().toString());
            }
        });

        kexue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_displayall(kexue.getText().toString());
            }
        });

        qita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_displayall(qita.getText().toString());
            }
        });

        return view;
    }

    //查询列表
    private void us_sc_displayall(){
        List<Book> mlist = new ArrayList<Book>();
        mlist = us_sc_helper.bookquery();

        UsersearchbookAdapter usersearchbookAdapter = new UsersearchbookAdapter(getContext(),mlist);
        user_searchbooks_listview.setAdapter(usersearchbookAdapter);
    }

    private void c_displayall(String c){
        List <Book> mlist = new ArrayList<>();
        mlist = us_sc_helper.categoryquery(c);

        UsersearchbookAdapter usersearchbookAdapter = new UsersearchbookAdapter(getContext(),mlist);
        user_searchbooks_listview.setAdapter(usersearchbookAdapter);
    }

    //根据书名、作者、isbn、类型查询
    private void book_search_displayall(String search_text){
        List<Book> mlist = new ArrayList<Book>();
        mlist = us_sc_helper.searchbookquery(search_text);

        UsersearchbookAdapter usersearchbookAdapter = new UsersearchbookAdapter(getContext(),mlist);
        user_searchbooks_listview.setAdapter(usersearchbookAdapter);

        //如果mlist为空，提示一下
        if(mlist.isEmpty()){
            Toasttext("搜索不到结果");
        }
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
}