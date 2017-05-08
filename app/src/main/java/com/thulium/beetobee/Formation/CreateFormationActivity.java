package com.thulium.beetobee.Formation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.thulium.beetobee.R;
import com.thulium.beetobee.WebService.MyFormationResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFormationActivity extends AppCompatActivity {

    private String[] arraySpinner;
    private Formation formation;
    private User user;
    private Button _createFormationButton;
    private EditText _title, _description, _date, _duree, _heure;
    private Spinner _spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_formation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create_formation);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        user = (User) getIntent().getSerializableExtra("user");

        _createFormationButton = (Button) findViewById(R.id.btn_create_formation);
        _title = (EditText) findViewById(R.id.input_title);
        _description = (EditText) findViewById(R.id.input_description);
        _date = (EditText) findViewById(R.id.input_date);
        _duree = (EditText) findViewById(R.id.input_duree);
        _heure = (EditText) findViewById(R.id.input_heure);


        _spinner = (Spinner) findViewById(R.id.spinner_type_formation);
        arraySpinner = new String[] {"Informatique", "Management", "Design"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        _spinner.setAdapter(adapter);
        _createFormationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validate()) {
                    Toast.makeText(getBaseContext(), "Champs incorrects", Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog progressDialog = new ProgressDialog(CreateFormationActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Création de la Formation...");
                progressDialog.show();

                final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
                Call<MyFormationResponse> call = requeteService.addFormation(formation,user.getAccess_token());
                call.enqueue(new Callback<MyFormationResponse>() {
                    @Override
                    public void onResponse(final Call<MyFormationResponse> call, final Response<MyFormationResponse> response) {
                        if (response.isSuccessful()) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Log.d("CreateFormationActivity", response.message());
                                            Toast.makeText(getBaseContext(), "Formation créée !", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), EspaceFormateurActivity.class);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }
                                    }, 500);
                        } else {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {

                                            Log.d("CreateFormationActivity", response.message());
                                            Toast.makeText(getBaseContext(), "Echec de création, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
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
                                        Log.d("CreateFormationActivity", t.getMessage());
                                        Toast.makeText(getBaseContext(), "Echec de création, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }, 500);
                    }
                });
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String title = _title.getText().toString();
        String description = _description.getText().toString();
        String date = _date.getText().toString();
        int duree = 0;
        if (_duree.getText().length() > 0)
        {
            duree = Integer.parseInt(_duree.getText().toString());
        }

        int heure = 0;
        if (_heure.getText().length() > 0)
        {
            heure = Integer.parseInt(_heure.getText().toString());
        }

        if (title.isEmpty() || title.length() < 2 || title.length() > 100) {
            _title.setError("Le titre doit être entre 2 et 100 caractères");
            valid = false;
        } else {
            _title.setError(null);
        }

        if (description.isEmpty() || description.length() < 2 || description.length() > 10000) {
            _description.setError("Le titre doit être entre 2 et 10000 caractères");
            valid = false;
        } else {
            _description.setError(null);
        }

        if (date.isEmpty()) {
            _date.setError("Date obligatoire");
            valid = false;
        } else {
            _date.setError(null);
        }

        if (duree < 1) {
            _duree.setError("Durée obligatoire");
            valid = false;
        } else {
            _duree.setError(null);
        }

        if (heure < 1) {
            _heure.setError("Durée obligatoire");
            valid = false;
        } else {
            _heure.setError(null);
        }

        if (valid)
        {

            formation = new Formation(title,description,duree,date);

            switch (_spinner.getSelectedItemPosition()){
                case 0:
                    formation.setThemeId(2);
                    break;
                case 1:
                    formation.setThemeId(3);
                    break;
                case 2:
                    formation.setThemeId(4);
                    break;
                default:
                    formation.setThemeId(0);
                    break;
            }
            formation.setHour(heure+" ");
            formation.setPlace("20");
        }

        return valid;
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
