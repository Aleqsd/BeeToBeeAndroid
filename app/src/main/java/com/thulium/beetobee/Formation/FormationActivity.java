package com.thulium.beetobee.Formation;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thulium.beetobee.BaseActivity;
import com.thulium.beetobee.Formation.Formation;
import com.thulium.beetobee.R;

import java.io.IOException;
import java.io.InputStream;

public class FormationActivity extends AppCompatActivity {
    private Formation formation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        formation = (Formation) getIntent().getSerializableExtra("formation");

        ImageView logo = (ImageView) findViewById(R.id.logoFormation);
        TextView title = (TextView) findViewById(R.id.titleFormation);
        TextView description = (TextView) findViewById(R.id.descriptionFormation);
        TextView duration = (TextView) findViewById(R.id.durationFormation);
        TextView date = (TextView) findViewById(R.id.dateFormation);
        TextView hour = (TextView) findViewById(R.id.hourFormation);
        TextView place = (TextView) findViewById(R.id.placeFormation);
        TextView availableSeat = (TextView) findViewById(R.id.availableSeatFormation);


        if (formation != null) {

            Toolbar toolbarFormation = (Toolbar) findViewById(R.id.toolbarFormation);
            toolbarFormation.setTitle(formation.getTitle());
            setSupportActionBar(toolbarFormation);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            toolbarFormation.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            title.setText(formation.getTitle());
            description.setText(formation.getDescription());
            duration.setText("Durée : "+formation.getDuration());
            date.setText("Date : "+formation.getDate().toString());
            hour.setText("à "+formation.getHour());
            place.setText("Place : "+formation.getPlace());
            availableSeat.setText("Places disponibles : "+formation.getAvailableSeat());
        }
        else // test
        {
            Toolbar toolbarFormation = (Toolbar) findViewById(R.id.toolbarFormation);
            toolbarFormation.setTitle("Formation Cryptographie");
            setSupportActionBar(toolbarFormation);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            toolbarFormation.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            InputStream ims = null;
            try {
                ims = getAssets().open("image7.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);

            logo.setImageDrawable(d);
            title.setText("Formation Cryptographie");
            description.setText("La cryptographie est une des disciplines de la cryptologie s'attachant à protéger des messages (assurant confidentialité, authenticité et intégrité) en s'aidant souvent de secrets ou clés.");
            duration.setText("Durée : 2 heures");
            date.setText("Date : 03/05/2017");
            hour.setText("à 16h");
            place.setText("Places : 20");
            availableSeat.setText("Places disponibles : 5");
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("FormationActivity", "onBackPressed Called");
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        startActivity(intent);
    }
}
