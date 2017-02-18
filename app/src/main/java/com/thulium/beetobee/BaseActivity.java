package com.thulium.beetobee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.thulium.beetobee.Formation.Test2Fragment;
import com.thulium.beetobee.Formation.TestFragment;
import com.thulium.beetobee.WebService.User;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BaseActivity";

    private Fragment fragment = null;
    private Class fragmentClass;
    private TabLayout tabLayout;
    private String profile_picture_path;
    public User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        TextView email = (TextView) hView.findViewById(R.id.email);
        TextView firstname = (TextView) hView.findViewById(R.id.firstname);
        ImageView avatar = (ImageView) hView.findViewById(R.id.avatar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.contains("profile_picture"))
        {
            profile_picture_path = settings.getString("profile_picture",null);
            Bitmap myBitmap = BitmapFactory.decodeFile(profile_picture_path);
            avatar.setImageBitmap(myBitmap);
            Log.d(TAG, "Profile picture set");
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    fragment = new Test2Fragment();
                }
                if (tab.getPosition() == 1) {
                    fragment = new TestFragment();
                }

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (fragment != null)
                    ft.replace(R.id.content_base, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();

// called when tab selected
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
// called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
// called when a tab is reselected
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //drawer.openDrawer(Gravity.START);
        navigationView.setNavigationItemSelectedListener(this);

        user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            firstname.setText(user.getFirstname());
            email.setText(user.getEmail());
            //The key argument here must match that used in the other activity
        }

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.getTabAt(1).select();
                    }
                }, 100);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragmentClass = TestFragment.class;
        } else if (id == R.id.nav_gallery) {
            fragmentClass = TestFragment.class;
        } else if (id == R.id.nav_manage) {
            fragmentClass = TestFragment.class;
        } else if (id == R.id.nav_share) {
            fragmentClass = TestFragment.class;
        } else if (id == R.id.nav_send) {
            fragmentClass = TestFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_base, fragment).commit();
        // Set action bar title
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
