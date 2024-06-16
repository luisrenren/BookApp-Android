package com.example.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdminbookAdapter extends BaseAdapter {
    //需要一个上下文 和 一个数据源
    private Context context;
    private List<Book> mlist;

    //需要一个构造方法，传值用的
    public AdminbookAdapter(Context context, List<Book> mlist) {
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
            view = LayoutInflater.from(context).inflate(R.layout.books_item,null);
            holder.tvimg = view.findViewById(R.id.tvimg);
            holder.tvtitle = view.findViewById(R.id.tvtitle);
            holder.tvisbn = view.findViewById(R.id.tvisbn);
            holder.tvis_recommend = view.findViewById(R.id.tvis_recommend);
            view.setTag(holder);
        }else{
            view=converView;
            holder=(ViewHolder) view.getTag();
        }
        holder.tvtitle.setText(mlist.get(i).getTitle());
        holder.tvisbn.setText(String.valueOf(mlist.get(i).getIsbn()));
        if(mlist.get(i).isIs_recommend()==true){
            holder.tvis_recommend.setText("√");
        }else {
            holder.tvis_recommend.setText("X");
        }
//        holder.tvis_recommend.setText(String.valueOf(mlist.get(i).isIs_recommend()));

        byte[] imagebyte = mlist.get(i).getBookimage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebyte,0,imagebyte.length);
        holder.tvimg.setImageBitmap(bitmap);

        return view;
    }

    class ViewHolder{
        ImageView tvimg;
        TextView tvtitle,tvisbn,tvis_recommend;
    }
}
