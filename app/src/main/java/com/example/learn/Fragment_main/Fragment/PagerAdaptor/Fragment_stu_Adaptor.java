package com.example.learn.Fragment_main.Fragment.PagerAdaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.learn.Fragment_main.Fragment.Notes_stu_list;
import com.example.learn.Fragment_main.Fragment.quiz_stu_list;
import com.example.learn.Fragment_main.Fragment.video_stu_list;

public class Fragment_stu_Adaptor extends FragmentStateAdapter {
    String sub_name;

    public Fragment_stu_Adaptor(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, @NonNull String name) {
        super(fragmentManager,lifecycle);
        this.sub_name = name;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                return new Notes_stu_list();
            case 2:
                return new quiz_stu_list(sub_name);
        }
        return new video_stu_list(sub_name);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
