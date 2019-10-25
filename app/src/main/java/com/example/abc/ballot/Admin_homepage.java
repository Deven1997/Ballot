    package com.example.abc.ballot;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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

        String dept = getIntent().getExtras().getString("dept_n");

        Bundle b = new Bundle();
        b.putString("dept_n",dept);

        ElectionList e = new ElectionList();
        Add_ElectionFragment a = new Add_ElectionFragment();

        e.setArguments(b);
        a.setArguments(b);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adding fragments
        adapter.AddFragments(e," Election List");  //dept pass to get the department of current user
        adapter.AddFragments(a, "Add Election"); //dept pass to get the department of current user

        //adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

        @Override
        public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(),StartPage.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // to finish all previous activities
                            startActivity(i);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
