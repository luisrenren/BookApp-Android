package com.example.library;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AdminBooks extends Fragment {
    ImageView book_image;
    EditText book_title,book_author,
            book_isbn,book_publisher,book_price,book_quantity,
            book_description,admin_searchbooks;
    CheckBox book_is_recommend;
    Spinner book_category;
    Button inputbookbtn;
    ListView book_listview;
    ImageButton admin_searchbtn;

    SQLiteDatabase db;
    BookOpenHelper helper;

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
                book_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_books,null);

        book_image = view.findViewById(R.id.book_image);
        book_category = view.findViewById(R.id.book_category);
        book_title = view.findViewById(R.id.book_title);
        book_author = view.findViewById(R.id.book_author);
        book_isbn = view.findViewById(R.id.book_isbn);
        book_publisher = view.findViewById(R.id.book_publisher);
        book_price = view.findViewById(R.id.book_price);
        book_quantity = view.findViewById(R.id.book_quantity);
        book_is_recommend = view.findViewById(R.id.book_is_recommend);
        book_description = view.findViewById(R.id.book_description);
        inputbookbtn = view.findViewById(R.id.inputbookbtn);
        book_listview = view.findViewById(R.id.book_listview);
        admin_searchbooks = view.findViewById(R.id.admin_searchbooks);
        admin_searchbtn = view.findViewById(R.id.admin_searchbtn);

        //给书本类型spinner设置列表
        String[] categories = {"科学", "教育", "文学", "其他"};
        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        book_category.setAdapter(adapter);
        //默认显示
        book_category.setSelection(3);

        //初始化时把searchbtn变true  true是搜索 false是返回
        admin_searchbtn.setSelected(true);
        admin_searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是搜索还是返回
                if (admin_searchbtn.isSelected()){
                    //如果是搜索，判断edittext是否为空
                    if(admin_searchbooks.getText().toString().equals("")){
                        Toasttext("请输入搜索的内容");
                    }else{
                        admin_searchbtn.setSelected(false);
                        book_search_displayall(admin_searchbooks.getText().toString());
                        //搜索完毕，输入框暂时变为不可用
                        admin_searchbooks.setEnabled(false);
                    }
                }else{
                    admin_searchbtn.setSelected(true);
                    //变回搜索后，把edittext清空
                    admin_searchbooks.setText("");
                    //列表也变回
                    book_displayall();
                    //搜索框变回可用
                    admin_searchbooks.setEnabled(true);
                }
            }
        });

        //改变按钮的颜色
        inputbookbtn.setBackgroundColor(Color.parseColor("#D5895C"));

        //调用MyOpenHelper的构造方法
        //得到了一个帮助类
        helper = new BookOpenHelper(getContext());
        //获取数据库
        db = helper.getReadableDatabase();

        //先载入一次图书信息
        book_displayall();

        //点击打开系统相册
        book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开系统相册
                //   /*表示要查找所有img图片
                launcher.launch("image/*");
            }
        });

        //点击按钮载入数据
        inputbookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判定是否为空
                if(book_title.getText().toString().equals("") || book_author.getText().toString().equals("") ||
                   book_isbn.getText().toString().equals("") || book_publisher.getText().toString().equals("") ||
                   book_price.getText().toString().equals("") || book_quantity.getText().toString().equals("") ||
                   book_description.getText().toString().equals("")){
                    Toasttext("请完整填写书籍信息");
                }else if(bitmap == null){
                    //判断有没有封面
                    Toasttext("请选择书籍封面");
                }else{
                    if(helper.insertbookdata(bitmap,
                            book_isbn.getText().toString(),
                            book_title.getText().toString(),
                            book_category.getSelectedItem().toString(),
                            book_author.getText().toString(),
                            book_publisher.getText().toString(),
                            book_price.getText().toString(),
                            book_quantity.getText().toString(),
                            book_is_recommend.isChecked(),
                            book_description.getText().toString())){
                        Toasttext("书籍添加成功！");
                        book_displayall();
                        empty_edittext();
                    }else {
                        Toasttext("书籍添加失败！");
                    }
                }
            }
        });

        //点按列表弹出修改详情页
        book_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取当前书籍的isbn
                TextView tvisbn = view.findViewById(R.id.tvisbn);

                Intent intent_changebook = new Intent();
                intent_changebook.setClass(getContext(),AdminChangebook.class);
                //把当前书籍的isbn传到修改页
                intent_changebook.putExtra("Isbn",tvisbn.getText().toString());
                startActivity(intent_changebook);
            }
        });

        //长按列表弹出是否删除判定
        book_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //这里把isbn作为唯一标识
                TextView tvisbn = view.findViewById(R.id.tvisbn);
                String isbn = tvisbn.getText().toString();

                AlertDialog delete_dlg = new AlertDialog.Builder(getContext())
                        .setTitle("是否删除该书籍信息？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(helper.delete_bookdata(isbn)){
                                    Toasttext("删除书籍成功！");
                                    //更新页面
                                    book_displayall();
                                }else{Toasttext("删除书籍失败！");}
                            }
                        })
                        .setNegativeButton("否",null)
                        .show();
                return true;
            }
        });

        return view;
    }

    //查询Book列表
    private void book_displayall(){
        List<Book> mlist = new ArrayList<Book>();
        mlist = helper.bookquery();

        AdminbookAdapter adminbookAdapter = new AdminbookAdapter(getContext(),mlist);
        book_listview.setAdapter(adminbookAdapter);
    }

    //根据书名、作者、isbn、类型查询
    private void book_search_displayall(String search_text){
        List<Book> mlist = new ArrayList<Book>();
        mlist = helper.searchbookquery(search_text);

        AdminbookAdapter adminbookAdapter = new AdminbookAdapter(getContext(),mlist);
        book_listview.setAdapter(adminbookAdapter);

        //如果mlist为空，提示一下
        if(mlist.isEmpty()){
            Toasttext("搜索不到结果");
        }
    }

    //添加成功后清空edittext
    private void empty_edittext(){
        book_image.setImageResource(R.drawable.inputimage);
        book_title.setText("");
        book_author.setText("");
        book_isbn.setText("");
        book_publisher.setText("");
        book_price.setText("");
        book_quantity.setText("");
        book_description.setText("");
        book_is_recommend.setChecked(false);
        book_category.setSelection(3);
    }

    private void Toasttext(String text){
        Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
}