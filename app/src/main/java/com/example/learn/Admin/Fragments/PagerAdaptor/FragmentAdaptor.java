package com.example.learn.Admin.Fragments.PagerAdaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.learn.Admin.Fragments.notesList;
import com.example.learn.Admin.Fragments.quizList;
import com.example.learn.Admin.Fragments.videoList;

public class FragmentAdaptor extends FragmentStateAdapter {

    String sub_name;

    public FragmentAdaptor(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,@NonNull String name) {
        super(fragmentManager,lifecycle);
        this.sub_name = name;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                return new notesList();
            case 2:
                return new quizList(sub_name);
        }
        return new videoList(sub_name);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
