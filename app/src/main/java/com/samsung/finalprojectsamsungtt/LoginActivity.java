package com.samsung.finalprojectsamsungtt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    DBTickets DBConnector;
    EditText email;
    EditText password;
    Button login;
    Button register;
    String accEmail;
    String accPassword;
    String root = "root@admin.com";
    String rootPas = "Root";
    Account acc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.loginButtonRegister);
        DBConnector = new DBTickets(this);

        addRoot(root, rootPas);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login(email.getText().toString(), password.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            switch (resultCode) {
                case RESULT_OK:
                    accEmail = data.getStringExtra("email");
                    accPassword = data.getStringExtra("password");
                    DBConnector.insertAcc(accEmail, accPassword, 0);
                    Login(accEmail, accPassword);
                    break;
            }
        }
    }

    public void Login(String email, String password) {
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
            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
        } else if (acc == null) {
            Toast.makeText(getApplicationContext(), "Account not found", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent();
            i.putExtra("email", email);
            i.putExtra("password", password);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    public void addRoot(String email, String password) {
        boolean newEmail = true;
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail())) {
                newEmail = false;
                break;
            }
        }
        if (newEmail) {
            DBConnector.insertAcc(email, password, 1);
        }
    }
}
