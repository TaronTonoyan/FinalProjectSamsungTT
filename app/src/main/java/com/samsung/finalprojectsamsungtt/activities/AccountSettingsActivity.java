package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;

public class AccountSettingsActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private EditText password;
    private EditText confirmPassword;
    private EditText address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initViews();
    }

    private void initViews() {
        ImageView back = findViewById(R.id.backAccount);
        TextView email = findViewById(R.id.accountEmailAddress);
        password = findViewById(R.id.accountPassword);
        confirmPassword = findViewById(R.id.accountNewPassword);
        address = findViewById(R.id.accountAddress);
        Button save = findViewById(R.id.accountSave);
        Button delete = findViewById(R.id.accountDelete);
        DBConnector = new DBShop(this);
        long id = getIntent().getLongExtra(getString(R.string.account), -1);
        Account acc = DBConnector.selectAcc(id);

        email.setText(acc.getEmail());
        address.setText(acc.getAddress());

        back.setOnClickListener(v -> finish());
        save.setOnClickListener(v -> {
            if (password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().equals("")) {
                acc.setPassword(password.getText().toString());
                acc.setAddress(address.getText().toString());
                DBConnector.updateAcc(acc);
                finish();
            }
        });
        delete.setOnClickListener(v -> {
            Intent intent = new Intent(AccountSettingsActivity.this, SureActivity.class);
            intent.putExtra(getString(R.string.yes), acc.getId());
            intent.putExtra(getString(R.string.no), 0);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

}
