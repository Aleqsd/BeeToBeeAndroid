package com.thulium.beetobee;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class NewsActivity extends AppCompatActivity {
    //private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //news = (News) getIntent().getSerializableExtra("news");

        ImageView logo = (ImageView) findViewById(R.id.logoNews);
        TextView title = (TextView) findViewById(R.id.titleNews);
        TextView description = (TextView) findViewById(R.id.descriptionNews);

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


}
