    package com.example.abc.ballot;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


    public class Admin_homepage extends AppCompatActivity {

        private TabLayout tabLayout;
        private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.pager_id);




        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adding fragments
        adapter.AddFragments(new ElectionList(),"Election List");
        adapter.AddFragments(new Add_ElectionFragment(), "Add Election");
        //adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
