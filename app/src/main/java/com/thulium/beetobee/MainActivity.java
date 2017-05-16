package com.thulium.beetobee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thulium.beetobee.Formation.CommonFragment;
import com.thulium.beetobee.Formation.CustPagerTransformer;
import com.thulium.beetobee.Formation.Formation;
import com.thulium.beetobee.WebService.AllFormationResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar myToolbar;
    private TextView indicatorTv;
    private AllFormationResponse currentResponse;
    public User user;
    private ViewPager viewPager;
    private List<CommonFragment> fragments = new ArrayList<>(); // ViewPager
    private String[] imageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.toolbarListeFormation);
        myToolbar.setTitle("Liste Formations");
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user = (User) getIntent().getSerializableExtra("user");
        currentResponse = new AllFormationResponse();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Récupération des formations");
        progressDialog.show();

        RequeteService requeteService = RestService.getClient().create(RequeteService.class);
        Call<AllFormationResponse> call2 = requeteService.getAllFormation();
        call2.enqueue(new Callback<AllFormationResponse>() {
            @Override
            public void onResponse(final Call<AllFormationResponse> call, final Response<AllFormationResponse> response) {
                if (response.isSuccessful()) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    currentResponse = response.body();
                                    Log.d(TAG, response.message());
                                    initImageLoader();
                                    fillViewPager(currentResponse);
                                    progressDialog.dismiss();
                                }
                            }, 100);
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Log.d(TAG, response.message());
                                    progressDialog.dismiss();
                                }
                            }, 100);
                }
            }
            @Override
            public void onFailure(Call<AllFormationResponse> call, final Throwable t) {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Log.d(TAG, t.getMessage());
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });


        //ToDo remplir l'imageArray en fonction des elements de currentResponse et de leur theme
    }

    /**
     * ViewPager
     */
    private void fillViewPager(final AllFormationResponse currentResponse) {
        final int count = currentResponse.getFormations().length;
        indicatorTv = (TextView) findViewById(R.id.indicator_tv);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        List<String> listImageArray = new ArrayList<String>();

        // 1. viewPager parallax PageTransformer
        viewPager.setPageTransformer(false, new CustPagerTransformer(this));

        // 2. viewPager adapter
        for (int i = 0; i < count; i++) {
            CommonFragment test = new CommonFragment();

            Bundle args = new Bundle();
            args.putInt("id",currentResponse.getFormations()[i].getId());
            args.putString("title", currentResponse.getFormations()[i].getTitle());
            args.putString("description", currentResponse.getFormations()[i].getDescription());
            args.putString("creatorFirstName", currentResponse.getFormations()[i].getCreator().getFirstName());
            args.putString("creatorLastName", currentResponse.getFormations()[i].getCreator().getLastName());
            args.putInt("duration", currentResponse.getFormations()[i].getDuration());
            args.putString("date", currentResponse.getFormations()[i].getDate());
            args.putString("hour", currentResponse.getFormations()[i].getHour());
            args.putString("place", currentResponse.getFormations()[i].getPlace());
            args.putInt("availableSeat", currentResponse.getFormations()[i].getAvailableSeat());
            args.putInt("userId",user.getId());
            args.putInt("themeId",currentResponse.getFormations()[i].getThemes().get(0).getId());
            args.putString("access_token",user.getAccess_token());
            args.putInt("creatorId",currentResponse.getFormations()[i].getCreatorId());
            args.putSerializable("user",user);

            ArrayList<Integer> userIds = new ArrayList<Integer>();
            ArrayList<String> userPictures = new ArrayList<>();
            for (User user : currentResponse.getFormations()[i].getUsers())
            {
                userIds.add(user.getId());
                userPictures.add(user.getProfilePicture());
            }

            args.putIntegerArrayList("userIds",userIds);
            args.putStringArrayList("userPictures",userPictures);

            test.setArguments(args);
            fragments.add(test);

            //ToDo a modifier avec un vrai jeu de test
            int idTheme = currentResponse.getFormations()[i].getThemes().get(0).getId();
            switch (idTheme)
            {
                case 1 :
                    listImageArray.add("assets://graphique.png");
                    break;
                case 2 :
                    listImageArray.add("assets://informatique.png");
                    break;
                case 3 :
                    listImageArray.add("assets://commerce.png");
                    break;
                default:
                    listImageArray.add("assets://graphique.png");
                    break;
            }

            imageArray = listImageArray.toArray(new String[0]);

        }

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                CommonFragment fragment = fragments.get(position % 10);
                fragment.bindData(imageArray[position % imageArray.length]);
                return fragment;
            }

            @Override
            public int getCount() {
                return count;
            }
        });


        // 3. viewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateIndicatorTv();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateIndicatorTv();
    }

    private void updateIndicatorTv() {
        int totalNum = viewPager.getAdapter().getCount();
        int currentItem = viewPager.getCurrentItem() + 1;
        indicatorTv.setText(Html.fromHtml("<font color='#12edf0'>" + currentItem + "</font>  /  " + totalNum));
        indicatorTv.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        // 2.ImageLoader
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
