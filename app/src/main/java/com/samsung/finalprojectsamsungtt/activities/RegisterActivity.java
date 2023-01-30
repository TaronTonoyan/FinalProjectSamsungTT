package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private CheckBox showPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Register");
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        showPassword = findViewById(R.id.showPassword);
        Button register = findViewById(R.id.register);
        DBConnector = new DBShop(this);

        showPassword.setOnClickListener(v -> {
            if (showPassword.isChecked()) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        register.setOnClickListener(v -> {
            if (password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().toString().equals("") && !email.getText().toString().equals("")) {
                register(email.getText().toString(), password.getText().toString());
            } else if (!password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().toString().equals("") && !email.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.not_same_pas), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.fill_form), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String email, String password) {
        boolean newEmail = true;
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail())) {
                newEmail = false;
                break;
            }
        }
        if (newEmail) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.email), email);
            intent.putExtra(getString(R.string.password), password);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.email_exists), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
