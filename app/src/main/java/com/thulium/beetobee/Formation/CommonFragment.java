package com.thulium.beetobee.Formation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.thulium.beetobee.MainActivity;
import com.thulium.beetobee.ProfileActivity;
import com.thulium.beetobee.R;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.User;

import org.junit.runner.Describable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xmuSistone on 2016/9/18.
 */
public class CommonFragment extends Fragment implements DragLayout.GotoDetailListener {
    private ImageView imageView;
    private TextView address1, address2, address3, address4, address5;
    private RatingBar ratingBar;
    private CircleImageView head1, head2, head3, head4;
    private String imageUrl;
    private String title = "Unknown";
    private String description = "Unknown";
    private String creatorFirstName = "Unknown";
    private String creatorLastName = "Unknown";
    private int duration = 0;
    private String hour = "Unknown";
    private String date = "Unknwon";
    private String place = "Unknown";
    private int availableSeat = 0;
    private int id = 0;
    private int userId;
    private int creatorId;
    private ArrayList<Integer> userIds;
    private String access_token;
    private Formation currentFormation;
    private ArrayList<CircleImageView> avatars;
    private User user;
    private int themeId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            title = getArguments().getString("title");
            description = getArguments().getString("description");
            creatorFirstName = getArguments().getString("creatorFirstName");
            creatorLastName = getArguments().getString("creatorLastName");
            date = getArguments().getString("date");
            duration = getArguments().getInt("duration");
            hour = getArguments().getString("hour");
            place = getArguments().getString("place");
            availableSeat = getArguments().getInt("availableSeat");
            userId = getArguments().getInt("userId");
            access_token = getArguments().getString("access_token");
            userIds = getArguments().getIntegerArrayList("userIds");
            creatorId = getArguments().getInt("creatorId");
            user = (User) getArguments().getSerializable("user");
            themeId = getArguments().getInt("themeId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, null);

        DragLayout dragLayout = (DragLayout) rootView.findViewById(R.id.drag_layout);
        imageView = (ImageView) dragLayout.findViewById(R.id.image);

        ImageLoader.getInstance().displayImage(imageUrl, imageView);
        address1 = (TextView) dragLayout.findViewById(R.id.address1);
        address4 = (TextView) dragLayout.findViewById(R.id.address4);
        address5 = (TextView) dragLayout.findViewById(R.id.address5);
        ratingBar = (RatingBar) dragLayout.findViewById(R.id.rating);

        head1 = (CircleImageView) dragLayout.findViewById(R.id.head1);
        head2 = (CircleImageView) dragLayout.findViewById(R.id.head2);
        head3 = (CircleImageView) dragLayout.findViewById(R.id.head3);
        head4 = (CircleImageView) dragLayout.findViewById(R.id.head4);

        avatars = new ArrayList<>();
        avatars.add(head1);
        avatars.add(head2);
        avatars.add(head3);
        avatars.add(head4);

        switch (userIds.size()) {
            case 0:
                head1.setVisibility(View.INVISIBLE);
                head2.setVisibility(View.INVISIBLE);
                head3.setVisibility(View.INVISIBLE);
                head4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                head2.setVisibility(View.INVISIBLE);
                head3.setVisibility(View.INVISIBLE);
                head4.setVisibility(View.INVISIBLE);
                setPictures(1);
                break;
            case 2:
                head3.setVisibility(View.INVISIBLE);
                head4.setVisibility(View.INVISIBLE);
                setPictures(2);
                break;
            case 3:
                head4.setVisibility(View.INVISIBLE);
                setPictures(3);
                break;
            default:
                setPictures(4);
                break;
        }

        dragLayout.setGotoDetailListener(this);
        address1.setText(title);
        address4.setText(description);
        address5.setText(creatorFirstName+" "+creatorLastName);
        return rootView;
    }

    public void setPictures(int number)
    {
        for (int i = 0; i<number; i++)
        {
            final String url;
            final int finalI = i;

            if (i == 0)
                url = "https://api.beetobee.fr/users/" + creatorId + "/picture/dl";
            else {
                url = "https://api.beetobee.fr/users/" + userIds.get(i - 1) + "/picture/dl";
                if (userIds.get(i-1) == user.getId())
                {
                    avatars.get(finalI).setBorderWidth(3);
                    avatars.get(finalI).setBorderColor(Color.parseColor("#4D276B"));
                }
            }

            final RequeteService requeteService = RestService.getClient().create(RequeteService.class);

            new AsyncTask<Void, Long, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Call<ResponseBody> call = requeteService.downloadProfilePicture(url);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.d("CommonFragment", "server contacted and has file");
                                writeResponseBodyToDisk(response.body(),avatars.get(finalI));
                            } else {
                                Log.d("CommonFragment", "server contact failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("CommonFragment", "error");
                        }
                    });
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void gotoDetail() {

        currentFormation = new Formation(id,title,description,duration,date,hour,place,availableSeat,creatorId,themeId);

        Intent intent = new Intent(getContext(), FormationActivity.class);
        intent.putExtra("formation",currentFormation);
        intent.putExtra("userId",userId);
        intent.putExtra("access_token",access_token);
        intent.putExtra("userIds",userIds);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    public void bindData(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, ImageView imageView) {
        try {
            File futureStudioIconFile = new File(getContext().getExternalFilesDir(null) + File.separator + "img_" + System.currentTimeMillis() + ".jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            final ProgressDialog progressDialog = new ProgressDialog(this.getContext(), R.style.AppTheme_Dark_Dialog);
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
                    imageView.setImageBitmap(myBitmap);
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

}
