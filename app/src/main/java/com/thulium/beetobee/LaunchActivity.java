package com.thulium.beetobee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thulium.beetobee.WebService.MyResponse;
import com.thulium.beetobee.WebService.RestService;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "LaunchActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    @Bind(R.id.testTextView)
    TextView _testTextView;

    public String loggedFirstname;
    public String loggedEmail;
    public String auth_token;
    public int auth_id = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            String password = extras.getString("password");
            _emailText.setText(email);
            _passwordText.setText(password);
            //The key argument here must match that used in the other activity
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        getToken();

        if (auth_token != null && !(auth_token.equals("")) && auth_id != 0)
            loginWithToken();
    }

    public void loginWithToken() {

        Log.d(TAG, "LoginWithToken");

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LaunchActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        RestService restService = new RestService();
        restService.getService().loginWithToken(auth_id, auth_token, new Callback<MyResponse>() {
            @Override
            public void success(final MyResponse totalResponse, Response response) {
                if (response.getStatus() == 200) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    Log.d(TAG, totalResponse.getResponse());
                                    loggedEmail = totalResponse.getUser().getEmail();
                                    loggedFirstname = totalResponse.getUser().getFirstname();
                                    onLoginSuccess();
                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 500);
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    //onLoginSuccess();
                                    onLoginFailed();
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
                                onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 500);
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LaunchActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        RestService restService = new RestService();
        restService.getService().login(email, password, new Callback<MyResponse>() {
            @Override
            public void success(final MyResponse totalResponse, Response response) {
                if (totalResponse.getCode() == 200) {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    loggedEmail = totalResponse.getUser().getEmail();
                                    loggedFirstname = totalResponse.getUser().getFirstname();
                                    auth_token = totalResponse.getUser().getAccess_token();
                                    auth_id = totalResponse.getUser().getId();
                                    setToken();
                                    onLoginSuccess();
                                    progressDialog.dismiss();
                                }
                            }, 100);
                } else if (totalResponse.getCode() == 503){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onWrongLogin();
                                    progressDialog.dismiss();
                                }
                            }, 100);
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 100);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {

                                onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 100);
            }
        });


    }

    private void onWrongLogin() {
        Toast.makeText(getBaseContext(), getString(R.string.wrongInfos), Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        finish();
    }

    public void setToken() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("access_token", auth_token);
        editor.putInt("id", auth_id);
        editor.apply();
        Log.d(TAG, "register token : " + auth_token + " id : " + auth_id);
    }

    public void getToken() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        auth_token = settings.getString("access_token", "");
        auth_id = settings.getInt("id", 0);
        Log.d(TAG, "registered token : " + auth_token + " registered id : " + auth_id);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        if ((loggedEmail != null) && (loggedFirstname != null)) {
            intent.putExtra("loggedEmail", loggedEmail);
            intent.putExtra("loggedFirstname", loggedFirstname);
        }

        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.loginFailed), Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.enterValidEmail));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            _passwordText.setError(getString(R.string.between4and16char));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

