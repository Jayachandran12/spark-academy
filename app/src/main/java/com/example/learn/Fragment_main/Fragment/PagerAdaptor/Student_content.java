package com.example.learn.Fragment_main.Fragment.PagerAdaptor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.learn.Admin.Fragments.PagerAdaptor.FragmentAdaptor;
import com.example.learn.Admin.RecycleAdaptor.subjectAdaptor;
import com.example.learn.Admin.video.video_detailed_view;
import com.example.learn.Helper_class.Interface.VideoItemClickListener;
import com.example.learn.Helper_class.Model.filemodel;
import com.example.learn.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_content extends AppCompatActivity  {

    private TextView code,subname,desc,instructor;

    // [TAB LAYOUT]
    TabLayout tabLayout;
    ViewPager2 pager;
    Fragment_stu_Adaptor fadaptor;
    ImageView bm;
    FirebaseDatabase database;
    DatabaseReference rootref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_content);

        //other attributes
        code = (TextView) findViewById(R.id.subject_code_SC) ;
        subname = (TextView) findViewById(R.id.subject_name_SC);
        desc = (TextView) findViewById(R.id.subject_desc_SC);
        instructor =  (TextView) findViewById(R.id.staff_name_SC);
        bm = (ImageView) findViewById(R.id.bm_sc);

        // [TAB_LAYOUT]
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_SC);
        pager = (ViewPager2) findViewById(R.id.view_pager_SC);

        // getting data
        Intent intent = getIntent();
        String sub_code = intent.getStringExtra("subCode");
        String sub_name = intent.getStringExtra("subName");
        String sub_desc = intent.getStringExtra("subDesc");
        String sub_staff = intent.getStringExtra("Instructor");

        // setting data
        code.setText(sub_code);
        subname.setText(sub_name);
        desc.setText(sub_desc);
        instructor.setText(sub_staff);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_sc);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_sc);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
               if(scrollRange == -1){
                   scrollRange = appBarLayout.getTotalScrollRange();
               }
               if(scrollRange + verticalOffset <= 0){
                   collapsingToolbarLayout.setTitle(sub_name);
                   isShow=true;
               }else if(isShow){
                   collapsingToolbarLayout.setTitle(" ");
                   isShow=false;
               }
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        /** Here Im passing Third argument as sub_name to display current subject videos,slides,quiz **/
        // Pager Adaptor
        fadaptor = new Fragment_stu_Adaptor(fm,getLifecycle(),sub_name);
        pager.setAdapter(fadaptor);

        tabLayout.addTab(tabLayout.newTab().setText("Lectures"));
        tabLayout.addTab(tabLayout.newTab().setText("Notes"));
        tabLayout.addTab(tabLayout.newTab().setText("Quiz"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                //super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}