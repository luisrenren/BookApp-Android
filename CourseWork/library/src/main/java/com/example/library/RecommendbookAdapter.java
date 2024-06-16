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

public class RecommendbookAdapter extends BaseAdapter {
    //需要一个上下文 和 一个数据源
    private Context context;
    private List<Book> mlist;

    //需要一个构造方法，传值用的
    public RecommendbookAdapter(Context context, List<Book> mlist) {
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
            view = LayoutInflater.from(context).inflate(R.layout.recommend_item,null);
            holder.recimg = view.findViewById(R.id.recimg);
            holder.rectitle = view.findViewById(R.id.rectitle);
            holder.recauthor = view.findViewById(R.id.recauthor);
            holder.recisbn = view.findViewById(R.id.recisbn);

            view.setTag(holder);
        }else{
            view=converView;
            holder=(ViewHolder) view.getTag();
        }
        holder.rectitle.setText(mlist.get(i).getTitle());
        holder.recauthor.setText("作者："+String.valueOf(mlist.get(i).getAuthor()));
        holder.recisbn.setText(String.valueOf(mlist.get(i).getIsbn()));

        byte[] imagebyte = mlist.get(i).getBookimage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebyte,0,imagebyte.length);
        holder.recimg.setImageBitmap(bitmap);

        return view;
    }

    class ViewHolder{
        ImageView recimg;
        TextView rectitle,recisbn,recauthor;
    }
}
