package com.samsung.finalprojectsamsungtt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBTickets DBConnector;
    String accEmail;
    String accPassword;
    Account acc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBConnector = new DBTickets(this);
        goToLoginPage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            switch (resultCode) {
                case RESULT_OK:
                    accEmail = data.getStringExtra("email");
                    accPassword = data.getStringExtra("password");
                    Login(accEmail, accPassword);
                    break;
                default:
                    finish();
            }
        }
        if (requestCode == 2){
            switch (resultCode) {
                case RESULT_OK:
                    acc = null;
                    goToLoginPage();
                    break;
                default:
                    finish();
            }
        }
    }

    public void Login(String email, String password) {
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail()) && password.equals(arr.get(i).getPassword())) {
                acc = arr.get(i);
            }
        }
        if (acc == null) {
            Toast.makeText(getApplicationContext(), "Account not found", Toast.LENGTH_SHORT).show();
        } else if (acc.getEmail().toString().equals("root@admin.com")) {
            Intent i = new Intent(MainActivity.this, AdminActivity.class);
            i.putExtra("isRoot", true);
            startActivityForResult(i, 2);
        } else if (acc.getIsAdmin() == 1){
            Intent i = new Intent(MainActivity.this, AdminActivity.class);
            startActivityForResult(i, 2);
        }
    }

    public void goToLoginPage() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(i, 1);
    }

}