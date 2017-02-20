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
import android.widget.Button;
import android.widget.EditText;

import com.thulium.beetobee.R;
import com.thulium.beetobee.WebService.MyFormationResponse;
import com.thulium.beetobee.WebService.RequeteService;
import com.thulium.beetobee.WebService.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFormationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateFormationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFormationFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "CreateFormationFragment";
    private EditText editTextTitle;
    private EditText editTextDescription;

    private OnFragmentInteractionListener mListener;

    public CreateFormationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateFormationFragment.
     */
    public static CreateFormationFragment newInstance() {
        return new CreateFormationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.creation_formation_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        editTextTitle = (EditText) getView().findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) getView().findViewById(R.id.editTextDescription);
        Button buttonSubmit = (Button) getView().findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Formation...");
        progressDialog.show();

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        Formation formation = new Formation(title,description);

        final RequeteService requeteService = RestService.getClient().create(RequeteService.class);
        Call<MyFormationResponse> call = requeteService.addFormation(formation);
        call.enqueue(new Callback<MyFormationResponse>() {
            @Override
            public void onResponse(final Call<MyFormationResponse> call, final Response<MyFormationResponse> response) {
                if (response.isSuccessful()) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    Log.d(TAG, response.message());
                                    Snackbar snackbar = Snackbar.make(getView(), "Creation successful", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    progressDialog.dismiss();
                                }
                            }, 500);
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    //onLoginSuccess();
                                    Log.d(TAG, response.message());
                                    Snackbar snackbar = Snackbar.make(getView(), "Creation failed", Snackbar.LENGTH_LONG);
                                    snackbar.show();
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
                                // On complete call either onLoginSuccess or onLoginFailed
                                //onLoginSuccess();
                                Log.d(TAG, t.getMessage());
                                Snackbar snackbar = Snackbar.make(getView(), "Creation failed", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                progressDialog.dismiss();
                            }
                        }, 500);
            }
        });
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
