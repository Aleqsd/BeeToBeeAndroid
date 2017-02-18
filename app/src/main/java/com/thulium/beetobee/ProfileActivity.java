package com.thulium.beetobee;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.thulium.beetobee.WebService.MyResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;
import com.thulium.beetobee.WebService.UserRegister;
import com.thulium.beetobee.WebService.UserUpdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    public int YOUR_SELECT_PICTURE_REQUEST_CODE = 0;
    private Uri outputFileUri;
    public CircularImageView avatar;
    public User user;
    private String profile_picture_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        avatar = (CircularImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openImageIntent();
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        user = (User) getIntent().getSerializableExtra("user");

        if (user != null)
            setProfileInfos();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (settings.contains("profile_picture"))
        {
            profile_picture_path = settings.getString("profile_picture",null);
            Bitmap myBitmap = BitmapFactory.decodeFile(profile_picture_path);
            avatar.setImageBitmap(myBitmap);
            Log.d(TAG, "Profile picture set");
        }
        else
        {
            final String url = "https://api.beetobee.fr/users/"+user.getId()+"/picture/dl";
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

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "img_" + System.currentTimeMillis() + ".jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, R.style.AppTheme_Dark_Dialog);
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

                    long progress = fileSizeDownloaded/fileSize;
                    progressDialog.setProgress((int) progress);
                    //Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                progressDialog.dismiss();

                if(futureStudioIconFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(futureStudioIconFile.getAbsolutePath());
                    avatar.setImageBitmap(myBitmap);
                    Log.d(TAG, "profil_picture created in Shared Preference : "+futureStudioIconFile.getAbsolutePath());
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

    private void setProfileInfos() {
        LinearLayout leBas = (LinearLayout) findViewById(R.id.leBas);

        View view = LayoutInflater.from(this).inflate(R.layout.profile_infos, null);
        TextView textView1 = (TextView) view.findViewById(R.id.textView);
        TextView textView3 = (TextView) view.findViewById(R.id.textView3);
        TextView textView4 = (TextView) view.findViewById(R.id.textView4);
        TextView textView5 = (TextView) view.findViewById(R.id.textView5);
        TextView textView6 = (TextView) view.findViewById(R.id.textView6);
        TextView textView7 = (TextView) view.findViewById(R.id.textView7);
        TextView textView8 = (TextView) view.findViewById(R.id.textView8);
        TextView textView9 = (TextView) view.findViewById(R.id.textView9);
        TextView textView10 = (TextView) view.findViewById(R.id.textView10);
        TextView textView11 = (TextView) view.findViewById(R.id.textView11);
        TextView textView12 = (TextView) view.findViewById(R.id.textView12);
        TextView textView13 = (TextView) view.findViewById(R.id.textView13);
        TextView textView14 = (TextView) view.findViewById(R.id.textView14);
        TextView textView15 = (TextView) view.findViewById(R.id.textView15);
        TextView textView16 = (TextView) view.findViewById(R.id.textView16);
        TextView textView17 = (TextView) view.findViewById(R.id.textView17);
        TextView textView18 = (TextView) view.findViewById(R.id.textView18);

        textView1.setText(user.getEmail());
        textView3.setText(user.getFirstname());
        textView4.setText(user.getLastname());
        textView5.setText(user.getPassword());
        if (user.getBirthDate() != null)
            textView6.setText(user.getBirthDate().toString());
        textView7.setText(user.getProfilePicture());
        textView8.setText(user.getSkypeId());
        textView9.setText(user.getCity());
        textView10.setText(user.getUniversity());
        textView11.setText(user.getEducation());
        if (user.getLevel() != 0)
            textView12.setText(user.getLevel());
        textView13.setText(user.getFbLink());
        textView14.setText(user.getTwitterLink());
        textView15.setText(user.getAccess_token());
        textView16.setText(user.getCreatedAt());
        textView17.setText(user.getUpdatedAt());
        if (user.getRoleId() != 0)
            textView18.setText(user.getRoleId());

        leBas.addView(view);

    }

    private void setProfileEditableInfos() {
        LinearLayout leBas = (LinearLayout) findViewById(R.id.leBas);

        leBas.removeAllViews();

        final View view = LayoutInflater.from(this).inflate(R.layout.profile_edit_infos, null);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        editText.setText(user.getFirstname());

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating...");
                progressDialog.show();

                UserUpdate ourUser = new UserUpdate();
                ourUser.setFirstname(editText.getText().toString());
                RequeteService requeteService = RestService.getClient().create(RequeteService.class);
                Call<MyResponse> call = requeteService.updateUser(ourUser, user.getId(), user.getAccess_token());
                call.enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(final Call<MyResponse> call, final Response<MyResponse> response) {
                        Log.d(TAG, "Update, Response code : " + response.code());
                        if (response.isSuccessful()) {
                            new Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // ToDo si update, mettre à jour les infos de l'user dans les UI, faire l'update
                                            Log.d(TAG, response.raw().request().toString());
                                            Log.d(TAG, response.message());
                                            Snackbar snackbar = Snackbar.make(view, "Update successful", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                            progressDialog.dismiss();
                                        }
                                    }, 100);
                        } else {
                            new Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Log.d(TAG, response.message());
                                            Snackbar snackbar = Snackbar.make(view, "Update failed", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                            progressDialog.dismiss();
                                        }
                                    }, 100);
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, final Throwable t) {
                        Log.d(TAG, "Login failure");
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Log.d(TAG, t.getMessage());
                                        Snackbar snackbar = Snackbar.make(view, "Update failed", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                        progressDialog.dismiss();
                                    }
                                }, 100);
                    }
                });
            }
        });

        leBas.addView(view);
    }

    private void openImageIntent() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        YOUR_SELECT_PICTURE_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                File image = null;
                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    avatar.setImageURI(selectedImageUri);
                    image = new File(selectedImageUri.getPath());
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    avatar.setImageURI(selectedImageUri);
                    image = new File(getRealPathFromURI(getApplicationContext(), selectedImageUri));
                }

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), image);
                MultipartBody.Part body = MultipartBody.Part.createFormData("picture", image.getName(), requestFile);

                RequeteService requeteService = RestService.getClient().create(RequeteService.class);
                Call<ResponseBody> call = requeteService.uploadProfilePicture(user.getId(), body,user.getAccess_token());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.v("Upload", "success");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                    }
                });

            }
        }
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_disconnect) {
            Log.d("Profile", "Disconnect pressed");
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.action_edit) {
            setProfileEditableInfos();
        }

        if (item.getItemId() == android.R.id.home) {
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
