package com.rxandroid;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rxandroid.fragment.ComputationFragment;
import com.rxandroid.fragment.ConcatMapFragment;
import com.rxandroid.fragment.FlatMapFragment;
import com.rxandroid.fragment.ImmediateFragment;
import com.rxandroid.fragment.IoFragment;
import com.rxandroid.fragment.MapFragment;
import com.rxandroid.fragment.NewThreadFragment;
import com.rxandroid.fragment.TrampolineFragment;
import com.rxandroid.fragment.ZipAndMergeFragment;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
//    private Button mParseJsonButton;
//    private Button mTryZipBtn;
//    private TextView mResultTextView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle = "Title";
    private String mDrawerTitle = "mDrawerTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setUpToolBar();
        setNavigationDrawer();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void setNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null;
                int itemId = menuItem.getItemId();
                switch (itemId){
                    case R.id.map:
                        frag =  MapFragment.newInstance();
                        setFragment(frag);
                        return true;
                    case R.id.flatMap:
                        frag =  FlatMapFragment.newInstance();
                        setFragment(frag);
                        return true;
                    case R.id.concatMap:
                        frag =  ConcatMapFragment.newInstance();
                        setFragment(frag);
                        return true;
                    case R.id.zip_and_merge:
                        frag =  ZipAndMergeFragment.newInstance();
                        setFragment(frag);
                        return true;
//                    case R.id.io:
//                        frag =  IoFragment.newInstance();
//                        setFragment(frag);
//                        return true;
                    case R.id.new_thred:
                        frag =  NewThreadFragment.newInstance();
                        setFragment(frag);
                        return true;
//                    case R.id.computation:
//                        frag =  ComputationFragment.newInstance();
//                        setFragment(frag);
//                        return true;
//                    case R.id.tempoline:
//                        frag =  TrampolineFragment.newInstance();
//                        setFragment(frag);
//                        return true;
//                    case R.id.immediate:
//                        frag =  ImmediateFragment.newInstance();
//                        setFragment(frag);
//                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, fragment);
        transaction.commit();
        mDrawerLayout.closeDrawers();
    }


    private void setUpToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
    }

}
