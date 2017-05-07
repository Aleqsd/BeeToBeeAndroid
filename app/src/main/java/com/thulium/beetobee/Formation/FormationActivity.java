package com.thulium.beetobee.Formation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thulium.beetobee.BaseActivity;
import com.thulium.beetobee.Formation.Formation;
import com.thulium.beetobee.R;
import com.thulium.beetobee.WebService.MyFormationResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.SimpleResponse;
import com.thulium.beetobee.WebService.User;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormationActivity extends AppCompatActivity {
    private Formation formation;
    private int userId;
    private String access_token;
    private boolean userInscris = false;
    private boolean userCreator = false;
    private ArrayList<Integer> userIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        formation = (Formation) getIntent().getSerializableExtra("formation");
        userId = getIntent().getIntExtra("userId",0);
        access_token = getIntent().getStringExtra("access_token");
        userIds = getIntent().getIntegerArrayListExtra("userIds");

        TextView title = (TextView) findViewById(R.id.titleFormation);
        TextView description = (TextView) findViewById(R.id.descriptionFormation);
        TextView duration = (TextView) findViewById(R.id.durationFormation);
        TextView date = (TextView) findViewById(R.id.dateFormation);
        TextView hour = (TextView) findViewById(R.id.hourFormation);
        TextView place = (TextView) findViewById(R.id.placeFormation);
        TextView availableSeat = (TextView) findViewById(R.id.availableSeatFormation);
        Button buttonInscription = (Button) findViewById(R.id.buttonInscription);
        buttonInscription.setOnClickListener(participate);

        if (formation != null) {

            for(Integer id : userIds)
            {
                if (id == userId)
                    userInscris = true;
            }

            if (userInscris)
                buttonInscription.setText("Se désinscrire");

            if(userId == formation.getCreatorId())
                buttonInscription.setVisibility(View.INVISIBLE);

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

            date.setText("Date : "+formation.getDate().toString());
            title.setText(formation.getTitle());
            description.setText(formation.getDescription());
            duration.setText("Durée : "+formation.getDuration());
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

            title.setText("Formation Cryptographie");
            description.setText("La cryptographie est une des disciplines de la cryptologie s'attachant à protéger des messages (assurant confidentialité, authenticité et intégrité) en s'aidant souvent de secrets ou clés.");
            duration.setText("Durée : 2 heures");
            date.setText("Date : 03/05/2017");
            hour.setText("à 16h");
            place.setText("Places : 20");
            availableSeat.setText("Places disponibles : 5");
        }
    }

    View.OnClickListener participate = new View.OnClickListener() {
        public void onClick(View v) {

            if (userInscris)
            {
                final ProgressDialog progressDialog = new ProgressDialog(FormationActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Désinscription");
                progressDialog.show();

                final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
                Call<SimpleResponse> call = requeteService.deleteParticipateFormation(formation.getId(),userId,access_token);
                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(final Call<SimpleResponse> call, final Response<SimpleResponse> response) {
                        if (response.isSuccessful()) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Toast.makeText(getBaseContext(), "Désinscription réussie", Toast.LENGTH_LONG).show();
                                            Log.d("FormationActivity", response.message());
                                            progressDialog.dismiss();
                                        }
                                    }, 500);
                        } else {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Toast.makeText(getBaseContext(), "La désinscription a échouée, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                            Log.d("FormationActivity", response.message());
                                            Log.d("FormationActivity", "code : " +response.code());
                                            progressDialog.dismiss();
                                        }
                                    }, 500);
                        }
                    }
                    @Override
                    public void onFailure(Call<SimpleResponse> call, final Throwable t) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "La désinscription a échouée, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                        Log.d("FormationActivity", t.getMessage());
                                        progressDialog.dismiss();
                                    }
                                }, 500);
                    }
                });
            }
            else
            {
                final ProgressDialog progressDialog = new ProgressDialog(FormationActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Inscription à la formation");
                progressDialog.show();

                final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
                Call<MyFormationResponse> call = requeteService.participateFormation(formation.getId(),userId,access_token);
                call.enqueue(new Callback<MyFormationResponse>() {
                    @Override
                    public void onResponse(final Call<MyFormationResponse> call, final Response<MyFormationResponse> response) {
                        if (response.isSuccessful()) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Toast.makeText(getBaseContext(), "Inscription réussie", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }, 500);
                        } else {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Toast.makeText(getBaseContext(), "L'inscription a échouée, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }, 500);
                        }
                    }
                    @Override
                    public void onFailure(Call<MyFormationResponse> call, final Throwable t) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "L'inscription a échouée, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }, 500);
                    }
                });
            }
        }
    };

    @Override
    public void onBackPressed() {
        Log.d("FormationActivity", "onBackPressed Called");
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
