package com.thulium.beetobee.Formation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
    private TextView address1, address4, address5, address6;
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
        ImageView threeDot = (ImageView) dragLayout.findViewById(R.id.three_dot);

        ImageLoader.getInstance().displayImage(imageUrl, imageView);
        address1 = (TextView) dragLayout.findViewById(R.id.address1);
        address4 = (TextView) dragLayout.findViewById(R.id.address4);
        address5 = (TextView) dragLayout.findViewById(R.id.address5);
        address6 = (TextView) dragLayout.findViewById(R.id.address6);

        threeDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetail();
            }
        });
        //dragLayout.setGotoDetailListener(this);
        address1.setText(title);
        address5.setText(description);

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        if (date != null)
        {
            try {
                Date oneWayTripDate = input.parse(date);                 // parse input
                address6.setText("Le "+output.format(oneWayTripDate)+" à "+hour);    // format output
            } catch (ParseException e) {
                e.printStackTrace();
                address6.setText(date+" à "+hour);
            }
        }

        address4.setText("Places disponibles : "+availableSeat);
        return rootView;
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

}
