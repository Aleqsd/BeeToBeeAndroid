package com.thulium.beetobee.Formation;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.thulium.beetobee.R;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFormationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFormationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFormationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ListFormationFragment";
    private Formation formation;
    private static final String FORMATION_KEY = "describable_key";

    private OnFragmentInteractionListener mListener;

    public static ListFormationFragment newInstance(Formation formation) {
        ListFormationFragment fragment = new ListFormationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FORMATION_KEY, formation);
        fragment.setArguments(bundle);

        return fragment;
    }

    public ListFormationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // if extending Activity
        //setContentView(R.layout.activity_main);
        formation = (Formation) getArguments().getSerializable(FORMATION_KEY);


        return inflater.inflate(R.layout.list_formation_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 1. pass context and data to the custom adapter
        MyAdapter adapter = new MyAdapter(this.getContext(), generateData());

        ListView listView = null;
        // if extending Activity 2. Get ListView from activity_main.xml
        try {
            listView = (ListView) getView().findViewById(R.id.listeFormation);
        } catch (NullPointerException e) {
            Log.e(TAG,"ListView null");
        }

        // 3. setListAdapter
        listView.setAdapter(adapter);
    }

    private ArrayList<Formation> generateData(){

        final ArrayList<Formation> models = new ArrayList<>();

        models.add(new Formation(formation.getTitle(),formation.getDescription()));
        models.add(new Formation("BeeToBee","Alex"));
        models.add(new Formation("Guignol","Guillaume"));
        models.add(new Formation("Web","Nicolas"));
        models.add(new Formation("iOS","Ludovic"));
        models.add(new Formation("Design","TmoT"));
        models.add(new Formation("Design","Julie"));
        models.add(new Formation("Design","Alexia"));
        models.add(new Formation("Web","Simon"));
        models.add(new Formation("Web","Léo"));
        models.add(new Formation("Formation 10","1"));
        models.add(new Formation("Formation 11","2"));
        models.add(new Formation("Formation 12","3"));
        models.add(new Formation("Formation 13","4"));
        models.add(new Formation("Formation 14","5"));
        models.add(new Formation("Formation 15","6"));
        return models;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
