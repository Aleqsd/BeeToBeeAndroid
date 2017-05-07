package com.thulium.beetobee.Formation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thulium.beetobee.R;

import org.junit.runner.Describable;

import java.util.ArrayList;

/**
 * Created by xmuSistone on 2016/9/18.
 */
public class CommonFragment extends Fragment implements DragLayout.GotoDetailListener {
    private ImageView imageView;
    private TextView address1, address2, address3, address4, address5;
    private RatingBar ratingBar;
    private View head1, head2, head3, head4;
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
    private ArrayList<Integer> userIds;
    private String access_token;
    private Formation currentFormation;


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

        head1 = dragLayout.findViewById(R.id.head1);
        head2 = dragLayout.findViewById(R.id.head2);
        head3 = dragLayout.findViewById(R.id.head3);
        head4 = dragLayout.findViewById(R.id.head4);

        dragLayout.setGotoDetailListener(this);
        address1.setText(title);
        address4.setText(description);
        address5.setText(creatorFirstName+" "+creatorLastName);
        return rootView;
    }

    @Override
    public void gotoDetail() {
        //salut = getArguments().getString("formation");



/*
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.IMAGE_TRANSITION_NAME, imageView);
        startActivity(intent);

        DetailActivity nextFrag = new DetailActivity();
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), nextFrag)
                .addToBackStack(null)
                .commit();

        Activity activity = (Activity) getContext();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(imageView, DetailActivity.IMAGE_TRANSITION_NAME),
                new Pair(address1, DetailActivity.ADDRESS1_TRANSITION_NAME),
                new Pair(address2, DetailActivity.ADDRESS2_TRANSITION_NAME),
                new Pair(address3, DetailActivity.ADDRESS3_TRANSITION_NAME),
                new Pair(address4, DetailActivity.ADDRESS4_TRANSITION_NAME),
                new Pair(address5, DetailActivity.ADDRESS5_TRANSITION_NAME),
                new Pair(ratingBar, DetailActivity.RATINGBAR_TRANSITION_NAME),
                new Pair(head1, DetailActivity.HEAD1_TRANSITION_NAME),
                new Pair(head2, DetailActivity.HEAD2_TRANSITION_NAME),
                new Pair(head3, DetailActivity.HEAD3_TRANSITION_NAME),
                new Pair(head4, DetailActivity.HEAD4_TRANSITION_NAME)
        );
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, imageUrl);
        startActivity(intent);
        //ActivityCompat.startActivity(activity, intent, options.toBundle());*/

        currentFormation = new Formation(id,title,description,duration,date,hour,place,availableSeat);

        Intent intent = new Intent(getContext(), FormationActivity.class);
        intent.putExtra("formation",currentFormation);
        intent.putExtra("userId",userId);
        intent.putExtra("access_token",access_token);
        intent.putExtra("userIds",userIds);
        startActivity(intent);
    }

    public void bindData(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
