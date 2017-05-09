package com.thulium.beetobee;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thulium.beetobee.data.FeedItem;

import java.io.IOException;
import java.io.InputStream;

public class NewsActivity extends AppCompatActivity {
    //private News news;
    private FeedItem feedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        feedItem = (FeedItem) getIntent().getSerializableExtra("feedItem");

        Toolbar toolbarNews = (Toolbar) findViewById(R.id.toolbarNews);
        toolbarNews.setTitle(feedItem.getName());
        setSupportActionBar(toolbarNews);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //news = (News) getIntent().getSerializableExtra("news");

        ImageView logo = (ImageView) findViewById(R.id.logoNews);
        TextView title = (TextView) findViewById(R.id.titleNews);
        TextView description = (TextView) findViewById(R.id.descriptionNews);

        title.setText(feedItem.getName());
        description.setText(feedItem.getStatus());
        Picasso.with(getApplicationContext()).load(feedItem.getImge()).into(logo);

        /*
        if (news != null) {

            Toolbar toolbarNews = (Toolbar) findViewById(R.id.toolbarNews);
            toolbarNews.setTitle(news.getTitle());
            setSupportActionBar(toolbarNews);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            toolbarNews.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            title.setText(news.getTitle());
            description.setText(news.getDescription());
        }
        else // test
        {
            Toolbar toolbarNews = (Toolbar) findViewById(R.id.toolbarNews);
            toolbarNews.setTitle("Nouvelle Formation");
            setSupportActionBar(toolbarNews);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            toolbarNews.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            InputStream ims = null;
            try {
                ims = getAssets().open("image6.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);

            logo.setImageDrawable(d);
            title.setText("Une nouvelle formation est disponible !");
            description.setText("La cryptographie est une des disciplines de la cryptologie s'attachant à protéger des messages (assurant confidentialité, authenticité et intégrité) en s'aidant souvent de secrets ou clés.");
        }*/
    }

    @Override
    public void onBackPressed() {
        Log.d("FormationActivity", "onBackPressed Called");
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
