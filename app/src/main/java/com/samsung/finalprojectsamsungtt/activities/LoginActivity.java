package com.samsung.finalprojectsamsungtt.activities;


import org.apache.commons.validator.routines.EmailValidator;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private EditText email;
    private EditText password;
    private CheckBox rememberMe;
    private Account acc = null;

    private final int REGISTER_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        addRoot(getString(R.string.root_email), getString(R.string.root_password));
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Login");
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.rememberMe);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        DBConnector = new DBShop(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);

        login.setOnClickListener(view -> {
            mediaPlayer.start();
            login(email.getText().toString(), password.getText().toString());
        });
        register.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            //noinspection deprecation
            startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE);
        });
    }

    private void login(String email, String password) {
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        boolean wrongPassword = false;
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail()) && password.equals(arr.get(i).getPassword())) {
                acc = arr.get(i);
                break;
            } else if (email.equals(arr.get(i).getEmail()) && !password.equals(arr.get(i).getPassword())) {
                wrongPassword = true;
                break;
            }
        }
        if (wrongPassword) {
            Toast.makeText(getApplicationContext(), getString(R.string.wrong_pass), Toast.LENGTH_SHORT).show();
        } else if (acc == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.acc_not_found), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.email), email);
            intent.putExtra(getString(R.string.password), password);
            setResult(RESULT_OK, intent);
            intent.putExtra(getString(R.string.account), rememberMe.isChecked());
            finish();
        }
    }

    private void addRoot(String email, String password) {
        boolean newEmail = true;
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail())) {
                newEmail = false;
                break;
            }
        }
        if (newEmail) {
            DBConnector.insertAcc(email, password, 1, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                String accEmail = data.getStringExtra(getString(R.string.email));
                String accPassword = data.getStringExtra(getString(R.string.password));
                boolean isValid = EmailValidator.getInstance().isValid(accEmail);
                if (isValid) {
                    DBConnector.insertAcc(accEmail, accPassword, 0, null);
                    login(accEmail, accPassword);
                } else {
                    Toast.makeText(getApplicationContext(), "Your email is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
