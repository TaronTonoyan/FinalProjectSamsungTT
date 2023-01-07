package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private EditText email;
    private EditText password;
    private Account acc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        addRoot(getString(R.string.root_email), getString(R.string.root_password));
    }

    private void initViews() {
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        Button login = findViewById(R.id.loginButton);
        Button register = findViewById(R.id.loginButtonRegister);
        DBConnector = new DBShop(this);

        login.setOnClickListener(view -> login(email.getText().toString(), password.getText().toString()));
        register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                String accEmail = data.getStringExtra(getString(R.string.email));
                String accPassword = data.getStringExtra(getString(R.string.password));
                DBConnector.insertAcc(accEmail, accPassword, 0, null);
                login(accEmail, accPassword);
            }
        }
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
            Intent i = new Intent();
            i.putExtra(getString(R.string.email), email);
            i.putExtra(getString(R.string.password), password);
            setResult(RESULT_OK, i);
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
}
