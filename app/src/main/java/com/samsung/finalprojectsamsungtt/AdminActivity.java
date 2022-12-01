package com.samsung.finalprojectsamsungtt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button addAdmins;
    Button addActivity;
    Button logOut;
    boolean isRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addAdmins = findViewById(R.id.addAdmins);
        addActivity = findViewById(R.id.addActivity);
        logOut = findViewById(R.id.logOutFromAdmin);
        Intent j = new Intent();
        isRoot = j.getBooleanExtra("isRoot", true);

        addAdmins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRoot) {
                    Intent i = new Intent(AdminActivity.this, AddAdminsActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "You are not ROOT and you are not allowed to Add Admins", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, AddActivityActivity.class);
                startActivity(i);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

}
