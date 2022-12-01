package com.samsung.finalprojectsamsungtt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    DBTickets DBConnector;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button register;
    ImageView goBack;
    TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.registerEmailAddress);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmNewPassword);
        register = findViewById(R.id.registerButton);
        text = findViewById(R.id.registerText);
        goBack = findViewById(R.id.returnToLoginPage);
        DBConnector = new DBTickets(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().toString().equals("") && !email.getText().toString().equals("")) {
                    Register(email.getText().toString(), password.getText().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(), "please, fill the form completely", Toast.LENGTH_SHORT).show();
                }
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void Register(String email, String password) {
        boolean newEmail = true;
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail())) {
                newEmail = false;
                break;
            }
        }
        if (newEmail) {
            Intent i = new Intent();
            i.putExtra("email", email);
            i.putExtra("password", password);
            setResult(RESULT_OK, i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
        }
    }

}
