package com.thulium.beetobee.Formation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thulium.beetobee.BaseActivity;
import com.thulium.beetobee.MainActivity;
import com.thulium.beetobee.R;
import com.thulium.beetobee.WebService.FormationUpdate;
import com.thulium.beetobee.WebService.MyFormationResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.SimpleResponse;
import com.thulium.beetobee.WebService.User;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
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
    EditText _title;
    EditText _description;
    Spinner _spinner;
    EditText _heure;
    EditText _date;
    EditText _duree;
    Button button;
    private String[] arraySpinner;
    FormationUpdate formationUpdate;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        formation = (Formation) getIntent().getSerializableExtra("formation");
        user = (User) getIntent().getSerializableExtra("user");
        userId = getIntent().getIntExtra("userId",0);
        access_token = getIntent().getStringExtra("access_token");
        userIds = getIntent().getIntegerArrayListExtra("userIds");

        if(userId == formation.getCreatorId())
        {
            super.setTheme(R.style.AppTheme_DarkRed);
            userCreator = true;
        }
        else
            super.setTheme(R.style.AppTheme_Dark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        TextView title = (TextView) findViewById(R.id.titleFormation);
        TextView description = (TextView) findViewById(R.id.descriptionFormation);
        TextView duration = (TextView) findViewById(R.id.durationFormation);
        TextView date = (TextView) findViewById(R.id.dateFormation);
        TextView place = (TextView) findViewById(R.id.placeFormation);
        TextView availableSeat = (TextView) findViewById(R.id.availableSeatFormation);
        ImageView imageFormation = (ImageView) findViewById(R.id.imageFormation);
        Button buttonInscription = (Button) findViewById(R.id.buttonInscription);
        buttonInscription.setOnClickListener(participate);

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

        if(userCreator)
        {
            buttonInscription.setVisibility(View.INVISIBLE);
            toolbarFormation.setBackgroundResource(R.color.red);
        }

        switch (formation.getThemeId()){
            case 2:
                imageFormation.setImageResource(R.drawable.informatique1080);
                break;
            case 3:
                imageFormation.setImageResource(R.drawable.commerce1080);
                break;
            case 4:
                imageFormation.setImageResource(R.drawable.graphique1080);
                break;
            default:
                imageFormation.setImageResource(R.drawable.graphique1080);
                break;
        }

        for(Integer id : userIds)
        {
            if (id == userId)
                userInscris = true;
        }

        if (userInscris)
            buttonInscription.setText("Se désinscrire");




        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        if (formation.getDate() != null)
        {
            try {
                Date oneWayTripDate = input.parse(formation.getDate());                 // parse input
                date.setText("Date : " + output.format(oneWayTripDate)+" à "+formation.getHour()+ " heures");    // format output
            } catch (ParseException e) {
                e.printStackTrace();
                date.setText("Date : "+formation.getDate());
            }
        }

        title.setText(formation.getTitle());
        description.setText(formation.getDescription());
        duration.setText("Durée : "+formation.getDuration()+" minutes");
        place.setText("Place : "+formation.getPlace());
        availableSeat.setText("Places disponibles : "+formation.getAvailableSeat());


    }

    @Override
    public void setTheme(int resid) {
        boolean changeTheme = true;
        super.setTheme(changeTheme ? R.style.AppTheme_DarkRed : resid);
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
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

                Log.d("FormationActivity", formation.getId()+" "+userId);
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
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
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
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (userId == formation.getCreatorId())
            getMenuInflater().inflate(R.menu.menu_formation, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.formation_edit_setting:
                affichageEditMode();
                return true;
            case R.id.formation_delete_setting:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Voulez vous vraiment supprimer cette formation ?").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void affichageEditMode()
    {
        // remove your listview
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewFormation);
        ViewGroup parent = (ViewGroup) scrollView.getParent();
        parent.removeView(scrollView);
        // inflate your profile view (or get the reference to it if it's already inflated)
        View editView;
        if(userId == formation.getCreatorId())
            editView = getLayoutInflater().inflate(R.layout.formation_edit_infos_formateur, parent, false);
        else
            editView = getLayoutInflater().inflate(R.layout.formation_edit_infos, parent, false);
        // add it to the parent
        parent.addView(editView);

        _title = (EditText) findViewById(R.id.input_title_edit);
        _description = (EditText) findViewById(R.id.input_description_edit);
        _spinner = (Spinner) findViewById(R.id.spinner_type_formation_edit);
        arraySpinner = new String[] {"Informatique", "Management", "Design"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        _spinner.setAdapter(adapter);
        _date = (EditText) findViewById(R.id.input_date_edit);
        _heure = (EditText) findViewById(R.id.input_heure_edit);
        _duree = (EditText) findViewById(R.id.input_duree_edit);
        button = (Button) findViewById(R.id.btn_edit_formation);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!validate())
                {
                    Toast.makeText(getBaseContext(), "Champs incorrects", Toast.LENGTH_LONG).show();
                    return;
                }
                editFormation();
            }
        });



        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date oneWayTripDate = input.parse(formation.getDate());                 // parse input
            _date.setText(output.format(oneWayTripDate));    // format output
        } catch (ParseException e) {
            e.printStackTrace();
            _date.setText("Date : "+formation.getDate());
        }

        _title.setText(formation.getTitle());
        _description.setText(formation.getDescription());
        _duree.setText(Integer.toString(formation.getDuration()));
        _heure.setText(formation.getHour());

    }

    public void editFormation()
    {
            final ProgressDialog progressDialog = new ProgressDialog(FormationActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Edition de la formation...");
            progressDialog.show();

        final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
            Call<MyFormationResponse> call = requeteService.updateFormation(formationUpdate,formation.getId(),access_token);
            call.enqueue(new Callback<MyFormationResponse>() {
                @Override
                public void onResponse(final Call<MyFormationResponse> call, final Response<MyFormationResponse> response) {
                    if (response.isSuccessful()) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Log.d("FormationActivity", response.message());
                                        Toast.makeText(getBaseContext(), "Formation éditée !", Toast.LENGTH_LONG).show();
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

                                        Log.d("FormationActivity", response.message());
                                        Toast.makeText(getBaseContext(), "Echec de l'édition, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getBaseContext(), "Echec de l'édition, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }, 500);
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

        String heure = _heure.getText().toString();



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


        if (!isValidFormat("dd/MM/yyyy",date)) {
            _date.setError("La date doit être au format jj/mm/aaaa");
            valid = false;
        }
        else
            _date.setError(null);

        if (duree < 1) {
            _duree.setError("Durée obligatoire");
            valid = false;
        } else {
            _duree.setError(null);
        }

        if (heure.isEmpty() || heure.length() < 2 || heure.length() > 5) {
            _heure.setError("L'heure doit être au format hh:mm");
            valid = false;
        } else {
            _heure.setError(null);
        }

        if (valid)
        {

            formationUpdate = new FormationUpdate();

            formationUpdate.setTitle(title);
            formationUpdate.setDescription(description);
            formationUpdate.setDuration(duree);
            formationUpdate.setDate(date);
            formationUpdate.setHour(heure);

            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date oneWayTripDate = input.parse(date);                 // parse input
                formationUpdate.setDate(output.format(oneWayTripDate));    // format output
            } catch (ParseException e) {
                e.printStackTrace();
            }

            switch (_spinner.getSelectedItemPosition()){
                case 0:
                    formationUpdate.setThemeId(2);
                    break;
                case 1:
                    formationUpdate.setThemeId(3);
                    break;
                case 2:
                    formationUpdate.setThemeId(4);
                    break;
                default:
                    formationUpdate.setThemeId(0);
                    break;
            }

        }

        return valid;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    DeleteFormation();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public void DeleteFormation()
    {
        final ProgressDialog progressDialog = new ProgressDialog(FormationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Suppression");
        progressDialog.show();

        final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
        Call<SimpleResponse> call = requeteService.deleteFormation(formation.getId(),access_token);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(final Call<SimpleResponse> call, final Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Formation supprimée", Toast.LENGTH_LONG).show();
                                    Log.d("FormationActivity", response.message());
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
                                    Toast.makeText(getBaseContext(), "La formation n'a pas pu être supprimée, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                    Log.d("FormationActivity", response.message());
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
                                Toast.makeText(getBaseContext(), "La formation n'a pas pu être supprimée, contactez le support BeeToBee", Toast.LENGTH_LONG).show();
                                Log.d("FormationActivity", t.getMessage());
                                progressDialog.dismiss();
                            }
                        }, 500);
            }
        });
    }
}
