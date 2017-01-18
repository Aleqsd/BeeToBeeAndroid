package com.thulium.beetobee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
        restService.getService().getStudentById(1,new Callback<Student>() {
            @Override
            public void success(Student student, Response responde) {
                text1.setText(String.valueOf(student.Age));
                text2.setText(student.Name);
                text3.setText(student.Email);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(TestProfile.this, error.getMessage().toString(),Toast.LENGTH_LONG);
            }
        });
    }
}
