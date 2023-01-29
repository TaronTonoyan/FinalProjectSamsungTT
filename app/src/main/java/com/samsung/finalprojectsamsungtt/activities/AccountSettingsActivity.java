package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Account Settings");
        TextView email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        address = findViewById(R.id.address);
        Button save = findViewById(R.id.save);
        Button delete = findViewById(R.id.delete);
        DBConnector = new DBShop(this);
        long id = getIntent().getLongExtra(getString(R.string.account), -1);
        actionBar.setTitle(DBConnector.selectAcc(id).getEmail());
        Account acc = DBConnector.selectAcc(id);

        email.setText(acc.getEmail());
        address.setText(acc.getAddress());

        save.setOnClickListener(v -> {
            if (password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().toString().equals("")) {
                acc.setPassword(password.getText().toString());
                acc.setAddress(address.getText().toString());
                DBConnector.updateAcc(acc);
                finish();
            } else if (password.getText().toString().equals(confirmPassword.getText().toString()) && password.getText().toString().equals("")) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
