package com.thulium.beetobee.WebService;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.thulium.beetobee.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TestProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_profile);

        final TextView text1 = (TextView) findViewById(R.id.textView5);
        final TextView text2 = (TextView) findViewById(R.id.textView6);
        final TextView text3 = (TextView) findViewById(R.id.textView7);


        RestService restService = new RestService();
        restService.getService().getStudentById(1,new Callback<MyResponse>() {
            @Override
            public void success(MyResponse totalResponse, Response response) {

                text1.setText(totalResponse.getUser().getFirstname());
                text2.setText(totalResponse.getUser().getLastname());
                text3.setText(totalResponse.getUser().getEmail());

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(TestProfile.this, error.getMessage().toString(),Toast.LENGTH_LONG);
            }
        });

    }
}
