package com.thulium.beetobee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.thulium.beetobee.Formation.EspaceFormateurActivity;
import com.thulium.beetobee.Formation.Formation;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;
import com.thulium.beetobee.adapter.FeedListAdapter;
import com.thulium.beetobee.app.AppController;
import com.thulium.beetobee.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
    public Formation formation;
    public User user;
    public ImageView avatar;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

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
        toolbar.setTitle("News");
        setSupportActionBar(toolbar);

        // Get the user informations from login
        user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            firstname.setText(user.getFirstname());
            email.setText(user.getEmail());
            //The key argument here must match that used in the other activity
        }

/*
        // Getting the formation for the FormationList Fragment
        final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
        Call<MyFormationResponse> call = requeteService.getFormation(1);
        call.enqueue(new Callback<MyFormationResponse>() {
            @Override
            public void onResponse(final Call<MyFormationResponse> call, final Response<MyFormationResponse> response) {
                if (response.isSuccessful()) {
                    formation = response.body().getFormation();
                    Log.d(TAG, response.message());

                    FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<MyFormationResponse> call, final Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });*/

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

        /* TabListener
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
        });*/

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

        ListView feed_list = (ListView) findViewById(R.id.feed_list);
        feed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.putExtra("feedItem", feedItems.get((int)id));
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        feed_list.setAdapter(listAdapter);

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                    addLocalNews();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new com.android.volley.Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        addLocalNews();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

        MenuItem item = navigationView.getMenu().getItem(3);
        if(user.getRoleId() == 0) // Non formateur
            item.getSubMenu().getItem(1).setVisible(false); // Cacher Espace Formateur
        else
            item.getSubMenu().getItem(0).setVisible(false); // Cacher Devenir Formateur
    }


    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void addLocalNews() {

                FeedItem item = new FeedItem();
                item.setId(0);
                item.setName("Projet Voltaire");

                // Image might be null sometimes
                String image = "http://afci-newsoft.fr/wp-content/uploads/2016/11/score_voltaire.png";
                item.setImge(image);
                item.setStatus("LA CERTIFICATION VOLTAIRE : UNE VRAIE VALEUR AJOUTÉE A MENTIONNER SUR UN CV\n" +
                        "Le Certificat Voltaire certifie votre niveau en orthographe sur votre CV, à l’instar du TOEIC® ou du TOEFL® pour l’anglais. 60 % des règles mesurées relèvent de la grammaire. Le Certificat Voltaire s’attache aux difficultés que gèrent mal les correcteurs automatiques.\n");
                item.setProfilePic("http://is1.mzstatic.com/image/thumb/Purple122/v4/1a/b1/37/1ab13774-6fd0-50b5-9f68-251adf748296/source/175x175bb.jpg");
                item.setTimeStamp("1403375851930");

                // url might be null sometimes
                String feedUrl = null;
                item.setUrl(feedUrl);

                FeedItem item2 = new FeedItem();
                item2.setId(1);
                item2.setName("Cisco");

                // Image might be null sometimes
                String image2 = "http://www.atplacademy.com/wp/wp-content/uploads/2014/08/cisco_Banner.png";
                item2.setImge(image2);
                item2.setStatus("CISCO – CCNA Routing &amp; Switching \n"+"La certification CCNA – Cisco Certified Network Associate est à ce jour la certification la plus connue\n"+
                                "et la plus demandée dans le monde des réseaux informatiques.\n"+
                                "Cette certification permet de valider la capacité à installer, opérer et dépanner un réseau\n"+
                                "informatique pour TPE et PME.\n"+
                                "Elle permet également de valider une base de connaissances relativement large, passant de la \n"+
                                "couche physique, aux protocoles niveau 2, aux protocoles de routage et pour finir aux protocoles applicatifs.");
                item2.setProfilePic("https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Cisco_logo.svg/1200px-Cisco_logo.svg.png");
                item2.setTimeStamp("1403375851930");

                // url might be null sometimes
                String feedUrl2 = null;
                item2.setUrl(feedUrl2);


                FeedItem item3 = new FeedItem();
                item3.setId(2);
                item3.setName("Adobe");

                // Image might be null sometimes
                String image3 = "https://www.courseandexam.com/media/catalog/category/Adobe-banner.jpg";
                item3.setImge(image3);
                item3.setStatus("La certification ACA (Adobe Certified Associate) valide les compétences acquises sur les outils de communication numérique Adobe. En obtenant la certification ACA, vous vous démarquez de la concurrence, prenez confiance en vous et découvrez de nouvelles opportunités professionnelles.\n"+"La certification ACA valide les compétences de base acquises dans les domaines de la communication, de la création et de la conception numérique avec les outils Adobe.");
                item3.setProfilePic("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Adobe_Systems_logo_and_wordmark.svg/2000px-Adobe_Systems_logo_and_wordmark.svg.png");
                item3.setTimeStamp("1403375851930");

                // url might be null sometimes
                String feedUrl3 = null;
                item3.setUrl(feedUrl3);

                feedItems.add(item2);
                feedItems.add(item);
                feedItems.add(item3);

        // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
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

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(getBaseContext(), "La demande a été transmise au support BeeToBee", Toast.LENGTH_LONG).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        
        int id = item.getItemId();

        if (id == R.id.nav_drawer_accueil) {
            Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_liste_formations) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_profil) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            /*
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            fragment = ListFormationFragment.newInstance(formation);
            tx.replace(R.id.content_base, fragment);
            tx.commit();*/
        }
        else if (id == R.id.devenir_formateur) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Voulez vous faire une demande afin de devenir formateur ?").setPositiveButton("Oui", dialogClickListener)
                    .setNegativeButton("Non", dialogClickListener).show();
        }
        else if (id == R.id.espace_formateur) {
            Intent intent = new Intent(getApplicationContext(), EspaceFormateurActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }


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
