package com.thulium.beetobee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thulium.beetobee.Formation.Formation;
import com.thulium.beetobee.R;

public class FormationActivity extends AppCompatActivity {
    private Formation formation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        formation = (Formation) getIntent().getSerializableExtra("formation");

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

        TextView title = (TextView) findViewById(R.id.titleFormation);
        TextView description = (TextView) findViewById(R.id.descriptionFormation);
        TextView duration = (TextView) findViewById(R.id.durationFormation);
        TextView date = (TextView) findViewById(R.id.dateFormation);
        TextView hour = (TextView) findViewById(R.id.hourFormation);
        TextView place = (TextView) findViewById(R.id.placeFormation);
        TextView availableSeat = (TextView) findViewById(R.id.availableSeatFormation);


        if (formation != null) {
            title.setText("Title : "+formation.getTitle());
            description.setText("Description : "+formation.getDescription());
            duration.setText("Duration : "+formation.getDuration());
            date.setText("Date : "+formation.getDate().toString());
            hour.setText("Hour : "+formation.getHour());
            place.setText("Place : "+formation.getPlace());
            availableSeat.setText("AvailableSeat : "+formation.getAvailableSeat());
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("FormationActivity", "onBackPressed Called");
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        startActivity(intent);
    }
}
