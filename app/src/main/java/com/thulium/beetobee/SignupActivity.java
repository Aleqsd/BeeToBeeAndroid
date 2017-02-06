package com.thulium.beetobee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thulium.beetobee.WebService.MyResponse;
import com.thulium.beetobee.WebService.RestService;
import com.thulium.beetobee.WebService.UserRegister;

import butterknife.ButterKnife;
import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_firstName)
    EditText _firstNameText;
    @Bind(R.id.input_lastName)
    EditText _lastNameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_reEnterEmail)
    EditText _reEnterEmail;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;

    public String email;
    public String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, getString(R.string.signup));

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creatingAccount));
        progressDialog.show();

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        UserRegister ourUser = new UserRegister(firstName, lastName, email, password);

        RestService restService = new RestService();
        restService.getService().addStudent(ourUser, new Callback<MyResponse>() {
            @Override
            public void success(MyResponse totalResponse, Response response) {
                if ((totalResponse.getCode() == 200)) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    onSignupSuccess();
                                    progressDialog.dismiss();
                                }
                            }, 500);
                } else if (totalResponse.getCode() == 500){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    //onLoginSuccess();
                                    emailAlreadyUsed();
                                    progressDialog.dismiss();
                                }
                            }, 500);
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    //onLoginSuccess();
                                    onSignupFailed();
                                    progressDialog.dismiss();
                                }
                            }, 500);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                //onLoginSuccess();
                                onSignupFailed();
                                progressDialog.dismiss();
                            }
                        }, 500);
            }
        });


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Account Created", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Connexion au serveur impossible", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void emailAlreadyUsed() {
        Toast.makeText(getBaseContext(), "Email déjà utilisée", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String reEnterEmail = _reEnterEmail.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 2 || firstName.length() > 30) {
            _firstNameText.setError(getString(R.string.between2and30char));
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 2 || lastName.length() > 30) {
            _lastNameText.setError(getString(R.string.between2and30char));
            valid = false;
        } else {
            _lastNameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.enterValidEmail));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (reEnterEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || !(reEnterEmail.equals(email))) {
            _reEnterEmail.setError(getString(R.string.emailDoNotMatch));
            valid = false;
        } else {
            _reEnterEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            _passwordText.setError(getString(R.string.between4and16char));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 16 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError(getString(R.string.passwordDoNotMatch));
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {

        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }
}