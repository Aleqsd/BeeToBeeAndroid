package com.thulium.beetobee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import com.thulium.beetobee.Formation.CreateFormationFragment;
import com.thulium.beetobee.Formation.Formation;
import com.thulium.beetobee.Formation.ListFormationFragment;
import com.thulium.beetobee.Formation.MyFormationResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base activity, after the login, with navigation drawer.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BaseActivity";
    private Fragment fragment = null;
    private Class fragmentClass;
    private TabLayout tabLayout;
    public Formation formation;
    public User user;
    public ImageView avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        TextView email = (TextView) hView.findViewById(R.id.email);
        TextView firstname = (TextView) hView.findViewById(R.id.firstname);
        avatar = (ImageView) hView.findViewById(R.id.avatar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the user informations from login
        user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            firstname.setText(user.getFirstname());
            email.setText(user.getEmail());
            //The key argument here must match that used in the other activity
        }

        // Create the tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));

        // Getting the formation for the FormationList Fragment
        final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
        Call<MyFormationResponse> call = requeteService.getFormation(1);
        call.enqueue(new Callback<MyFormationResponse>() {
            @Override
            public void onResponse(final Call<MyFormationResponse> call, final Response<MyFormationResponse> response) {
                if (response.isSuccessful()) {
                    formation = response.body().getFormation();
                    Log.d(TAG, response.message());
                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<MyFormationResponse> call, final Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

        // Getting the profile picture from local or downloading it if the url exists for this user in database
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (settings.contains("profile_picture")) {
            String profile_picture_path = settings.getString("profile_picture", null);
            Bitmap myBitmap = BitmapFactory.decodeFile(profile_picture_path);
            avatar.setImageBitmap(myBitmap);
            Log.d(TAG, "Profile picture set");
        } else if (user.getProfilePicture() != null) {
            final String url = "https://api.beetobee.fr/users/" + user.getId() + "/picture/dl";
            final RequeteService requeteService2 = RestService.getClient().create(RequeteService.class);
            Call<ResponseBody> call2 = requeteService2.downloadProfilePicture(url);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "server contacted and has file");

                        boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                        Log.d(TAG, "file download was a success? " + writtenToDisk);
                    } else {
                        Log.d(TAG, "server contact failed");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "error");
                }
            });
        }

        // TabListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    fragment = new CreateFormationFragment();
                }
                if (tab.getPosition() == 1) {
                    if (formation != null)
                        fragment = ListFormationFragment.newInstance(formation);
                    else
                        fragment = new ListFormationFragment();
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

        // Avatar leading to profile page, passing the user object
        // ToDo Ergonomy : More obvious way to accessing Profile page ?
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        // Navigation drawer pre-coded stuffs
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //drawer.openDrawer(Gravity.START);
        navigationView.setNavigationItemSelectedListener(this);

        // Poping on tab2
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tabLayout.getTabAt(1).select();
                        } catch (NullPointerException e) {
                            Log.e(TAG, "error");
                        }
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
            fragmentClass = ListFormationFragment.class;
        } else if (id == R.id.nav_gallery) {
            fragmentClass = ListFormationFragment.class;
        } else if (id == R.id.nav_manage) {
            fragmentClass = ListFormationFragment.class;
        } else if (id == R.id.nav_share) {
            fragmentClass = ListFormationFragment.class;
        } else if (id == R.id.nav_send) {
            fragmentClass = ListFormationFragment.class;
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

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "img_" + System.currentTimeMillis() + ".jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            final ProgressDialog progressDialog = new ProgressDialog(BaseActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(false);
            progressDialog.setTitle("Downloading Profile Picture");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.setMax(100);
            progressDialog.show();

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    long progress = fileSizeDownloaded / fileSize;
                    progressDialog.setProgress((int) progress);
                    //Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                progressDialog.dismiss();

                if (futureStudioIconFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(futureStudioIconFile.getAbsolutePath());
                    avatar.setImageBitmap(myBitmap);
                    Log.d(TAG, "profil_picture created in Shared Preference : " + futureStudioIconFile.getAbsolutePath());
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("profile_picture", futureStudioIconFile.getAbsolutePath());
                    editor.apply();
                }


                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }

    }
}
