package com.example.learn.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.learn.Admin.Fragments.PagerAdaptor.FragmentAdaptor;
import com.example.learn.R;
import com.google.android.material.tabs.TabLayout;

public class Content extends AppCompatActivity{

    private TextView code,subname,desc,instructor;

    // [TAB LAYOUT]
    TabLayout tabLayout;
    ViewPager2 pager;
    FragmentAdaptor fadaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        //other attributes
        code = (TextView) findViewById(R.id.subject_code_AC) ;
        subname = (TextView) findViewById(R.id.subject_name_AC);
        desc = (TextView) findViewById(R.id.subject_desc_AC);
        instructor =  (TextView) findViewById(R.id.staff_name_AC);

        // [TAB_LAYOUT]
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_AC);
        pager = (ViewPager2) findViewById(R.id.view_pager_AC);

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

        FragmentManager fm = getSupportFragmentManager();
        /** Here Im passing Third argument as sub_name to display current subject videos,slides,quiz **/
        // Pager Adaptor
        fadaptor = new FragmentAdaptor(fm,getLifecycle(),sub_name);
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