package com.example.library;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentStateAdapter {
    List<Fragment> mlist;

    //给mlist赋值
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> mlist) {
        super(fragmentActivity);
        this.mlist = mlist;
    }
    //返回的fragment
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mlist.get(position);
    }
    //循环次数，有几个就循环几次
    @Override
    public int getItemCount() {
        return mlist.size();
    }
}