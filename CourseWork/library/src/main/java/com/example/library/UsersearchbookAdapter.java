package com.example.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UsersearchbookAdapter extends BaseAdapter {
    //需要一个上下文 和 一个数据源
    private Context context;
    private List<Book> mlist;

    //需要一个构造方法，传值用的
    public UsersearchbookAdapter(Context context, List<Book> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() { return mlist.size(); }

    @Override
    public Object getItem(int i) { return i; }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        View view;
        ViewHolder holder = new ViewHolder();
        if(converView == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_books_item,null);
            holder.us_sc_img = view.findViewById(R.id.us_sc_img);
            holder.us_sc_title = view.findViewById(R.id.us_sc_title);
            holder.us_sc_price = view.findViewById(R.id.us_sc_price);
            holder.us_sc_author = view.findViewById(R.id.us_sc_author);
            holder.us_sc_category = view.findViewById(R.id.us_sc_category);
            holder.us_sc_isbn = view.findViewById(R.id.us_sc_isbn);

            view.setTag(holder);
        }else{
            view=converView;
            holder=(ViewHolder) view.getTag();
        }
        holder.us_sc_title.setText(mlist.get(i).getTitle());
        holder.us_sc_price.setText("￥"+String.valueOf(mlist.get(i).getPrice()));
        holder.us_sc_author.setText("作者："+String.valueOf(mlist.get(i).getAuthor()));
        holder.us_sc_category.setText("类型："+String.valueOf(mlist.get(i).getCategory()));
        holder.us_sc_isbn.setText(String.valueOf(mlist.get(i).getIsbn()));

        byte[] imagebyte = mlist.get(i).getBookimage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebyte,0,imagebyte.length);
        holder.us_sc_img.setImageBitmap(bitmap);

        return view;
    }

    class ViewHolder{
        ImageView us_sc_img;
        TextView us_sc_title,us_sc_price,us_sc_isbn,us_sc_author,us_sc_category;
    }
}
