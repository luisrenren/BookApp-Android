package com.example.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyuserAdapter extends BaseAdapter {
    //需要一个上下文 和 一个数据源;
    private Context context;
    private List<User> mlist;

    //构造方法，传值用的
    public MyuserAdapter(Context context, List<User> mlist) {
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
    public View getView(int i, View converview, ViewGroup viewGroup) {
        View view;
        ViewHolder holder = new ViewHolder();
        if(converview == null){
            view = LayoutInflater.from(context).inflate(R.layout.users_item,null);
            holder.tvname = view.findViewById(R.id.tvname);
            holder.tvgender = view.findViewById(R.id.tvgender);
            holder.tvphone = view.findViewById(R.id.tvphone);
            holder.tvpassword = view.findViewById(R.id.tvpassword);
            holder.tvbirthday = view.findViewById(R.id.tvbirthday);
            view.setTag(holder);
        }else{
            view=converview;
            holder=(ViewHolder) view.getTag();
        }
        holder.tvname.setText(mlist.get(i).getUsername());
        holder.tvgender.setText(String.valueOf(mlist.get(i).getGender()));
        holder.tvphone.setText(String.valueOf(mlist.get(i).getPhone()));
        holder.tvpassword.setText(String.valueOf(mlist.get(i).getPassword()));
        holder.tvbirthday.setText(String.valueOf(mlist.get(i).getBirthday()));
        return view;
    }

    class ViewHolder{
        TextView tvname;
        TextView tvgender;
        TextView tvphone;
        TextView tvpassword;
        TextView tvbirthday;
    }
}
