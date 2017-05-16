package com.thulium.beetobee.Formation;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.thulium.beetobee.BaseActivity;
import com.thulium.beetobee.R;
import com.thulium.beetobee.WebService.AllFormationResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.mikhaellopez.circularimageview.CircularImageView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.thulium.beetobee.Formation.Formation;
import com.thulium.beetobee.Formation.FormationActivity;
import com.thulium.beetobee.WebService.AllFormationResponse;
import com.thulium.beetobee.WebService.MyResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;
import com.thulium.beetobee.WebService.UserUpdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.typeface;

public class EspaceFormateurActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    public User user;
    public TextView name;
    public CircularImageView avatar;
    public TextView desc;
    public CarouselView carouselView;
    private AllFormationResponse allFormation;
    public ArrayList<Integer> themes = new ArrayList<>();
    public int[] sampleImages;
    public List formationsUser = new ArrayList<>();
    private static final String TAG = "EspaceFormateurActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_formateur);
        myToolbar = (Toolbar) findViewById(R.id.toolbar_espace_formateur);
        myToolbar.setTitle("Espace Formateur");
        setSupportActionBar(myToolbar);
        avatar = (CircularImageView) findViewById(R.id.avatar_espace_formateur);
        Button buttonCreate = (Button) findViewById(R.id.button_creer_formation);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), CreateFormationActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user = (User) getIntent().getSerializableExtra("user");

        if (user != null)
            setProfileInfos();

        RequeteService requeteService2 = RestService.getClient().create(RequeteService.class);
        Call<AllFormationResponse> call2 = requeteService2.getAllFormation();
        call2.enqueue(new Callback<AllFormationResponse>() {
            @Override
            public void onResponse(final Call<AllFormationResponse> call2, final Response<AllFormationResponse> response) {
                if (response.isSuccessful()) {
                    allFormation = response.body();
                    Log.d(TAG, response.message());

                    if (allFormation.getFormations() != null)
                    {
                        for (Formation formation : allFormation.getFormations())
                        {
                            if (formation.getCreatorId() == user.getId())
                            {
                                formationsUser.add(formation);
                                AddImageCarousel(formation.getThemes().get(0).getId());
                            }
                        }
                    }

                    //ToDO tester le cas où pas de formations suivies
                    if(themes.size()!=0)
                    {
                        sampleImages = convertIntegers(themes);
                        carouselView = (CarouselView) findViewById(R.id.carouselView_espace_formateur);
                        carouselView.setImageListener(imageListener);
                        carouselView.setPageCount(sampleImages.length);

                        carouselView.setImageClickListener(new ImageClickListener() {
                            @Override
                            public void onClick(int position) {
                                Intent intent = new Intent(getApplicationContext(), FormationActivity.class);
                                intent.putExtra("formation", (Formation) formationsUser.get(position));
                                intent.putExtra("userId", user.getId());
                                intent.putExtra("access_token", user.getAccess_token());
                                ArrayList<Integer> userIds = new ArrayList<Integer>();
                                for (User user : ((Formation) formationsUser.get(position)).getUsers())
                                {
                                    userIds.add(user.getId());
                                }
                                intent.putExtra("userIds", userIds);
                                startActivity(intent);
                            }
                        });
                    }

                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<AllFormationResponse> call2, final Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (settings.contains("profile_picture")) {
            String profile_picture_path = settings.getString("profile_picture", null);
            Bitmap myBitmap = BitmapFactory.decodeFile(profile_picture_path);
            avatar.setImageBitmap(myBitmap);
            Log.d(TAG, "Profile picture set");
        } else {
            final String url = "https://api.beetobee.fr/users/" + user.getId() + "/picture/dl";
            final RequeteService requeteService = RestService.getClient().create(RequeteService.class);

            new AsyncTask<Void, Long, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Call<ResponseBody> call = requeteService.downloadProfilePicture(url);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "server contacted and has file");
                                //ToDo Limite à la taille du fichier ou au nombre d'upload par heure ou minutes ?
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
                    return null;
                }
            }.execute();
        }
    }
    private void AddImageCarousel(int idTheme)
    {
        switch (idTheme){
            case 2:
                themes.add(R.drawable.informatique);
                break;
            case 3:
                themes.add(R.drawable.commerce);
                break;
            case 4:
                themes.add(R.drawable.graphique);
                break;
            default:
                themes.add(R.drawable.informatique);
                break;
        }
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void setProfileInfos() {
        LinearLayout leBas = (LinearLayout) findViewById(R.id.leBas_espace_formateur);

        if (getIntent().getExtras().containsKey("update")) {
            Snackbar snackbar = Snackbar.make(leBas, "Update successful", Snackbar.LENGTH_LONG);
            snackbar.show();
            user = (User) getIntent().getSerializableExtra("user");
        }

        TextView lename = (TextView) findViewById(R.id.textView1_espace_formateur);
        TextView ledesc = (TextView) findViewById(R.id.textView2_espace_formateur);

        TextView text_education = (TextView) findViewById(R.id.text_education_espace_createur);
        TextView text_level = (TextView) findViewById(R.id.text_level_espace_createur);
        TextView text_email = (TextView) findViewById(R.id.text_email_espace_createur);

        if (user.getCity() != null)
            text_education.setText(user.getCity());
        if (user.getLevel() != null && user.getEducation() != null && user.getUniversity() != null)
            text_level.setText(user.getUniversity() + " " + user.getEducation() + " " + user.getLevel());
        text_email.setText(user.getEmail());


        lename.setText(user.getFirstname()+" "+user.getLastname());
        if (user.getEducation() != null)
            ledesc.setText(user.getEducation());
        else
            ledesc.setText("Étudiant Ynov");
    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "img_" + System.currentTimeMillis() + ".jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            final ProgressDialog progressDialog = new ProgressDialog(EspaceFormateurActivity.this, R.style.AppTheme_DarkRed);
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
                    SavePictureInPreferences(futureStudioIconFile.getAbsolutePath());
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

    private void SavePictureInPreferences(String profile_picture_path) {
        Log.d(TAG, "profil_picture created in Shared Preference : " + profile_picture_path);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("profile_picture", profile_picture_path);
        editor.apply();
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

    @Override
    public void onBackPressed() {
        Log.d("FormationActivity", "onBackPressed Called");
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
}
