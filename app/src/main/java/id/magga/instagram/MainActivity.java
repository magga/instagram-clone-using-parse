package id.magga.instagram;

import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    boolean loginMode = true;

    EditText etUsername;
    EditText etPassword;

    Button btnMode;
    TextView tvMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeView();

        InitializeParse();

        if(ParseUser.getCurrentUser() != null){
            Login();
        }
    }

    public void BtnClicked(View view){
        FetchUser();
    }

    private void FetchUser() {
        if(loginMode){
            // Code Buat Login

            ParseUser.logInInBackground(
                    etUsername.getText().toString(),
                    etPassword.getText().toString(),
                    new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null){
                                Toast.makeText(MainActivity.this,
                                        "Login Berhasil dengan email : " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();

                                Login();
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Login Gagal : " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
            );
        } else {
            // Code Buat Signup

            ParseUser user = new ParseUser();

            user.setUsername(etUsername.getText().toString());
            user.setPassword(etPassword.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Signup berhasil", Toast.LENGTH_SHORT).show();

                        Login();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Signup gagal : " + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    public void ChangeMode(View view){
        if (loginMode){
            loginMode = false;
            btnMode.setText("SIGNUP");
            tvMode.setText("Or, LOGIN");
        } else {
            loginMode = true;
            btnMode.setText("LOGIN");
            tvMode.setText("Or, SIGNUP");
        }
    }

    private void InitializeView() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    FetchUser();
                }

                return false;
            }
        });

        btnMode = (Button) findViewById(R.id.btnMode);
        tvMode = (TextView) findViewById(R.id.tvMode);
    }


    private void InitializeParse() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("YOUR_APP_ID")
                .server("YOUR_SERVER_URL")
                .build()
        );
    }

    public void Login(){
        Intent intent = new Intent(getApplicationContext(),
                HomeActivity.class);

        startActivity(intent);
    }

    public void HideInput(View view){
        InputMethodManager ipm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        ipm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
