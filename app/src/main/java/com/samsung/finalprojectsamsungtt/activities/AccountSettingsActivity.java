package com.samsung.finalprojectsamsungtt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private long id;

    private final int DELETE_ACTIVITY_REQUEST_CODE = 1;
    private final int CART_ACTIVITY_REQUEST_CODE = 2;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Account Settings");
        TextView email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        address = findViewById(R.id.address);
        Button save = findViewById(R.id.save);
        Button delete = findViewById(R.id.delete);
        DBConnector = new DBShop(this);
        id = getIntent().getLongExtra(getString(R.string.account), -1);
        Account acc = DBConnector.selectAcc(id);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);

        email.setText(acc.getEmail());
        address.setText(acc.getAddress());

        save.setOnClickListener(v -> {
            mediaPlayer.start();
            if (password.getText().toString().equals(confirmPassword.getText().toString()) && !password.getText().toString().equals("")) {
                acc.setPassword(password.getText().toString());
                acc.setAddress(address.getText().toString());
                DBConnector.updateAcc(acc);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.fill_form), Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(AccountSettingsActivity.this, SureActivity.class);
            intent.putExtra(getString(R.string.yes), acc.getId());
            intent.putExtra(getString(R.string.no), 0);
            //noinspection deprecation
            startActivityForResult(intent, DELETE_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -2) {
            goToLoginPage();
        }
        if (requestCode == DELETE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        if (requestCode == CART_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.order_delivered), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == LOG_OUT_CONFIRMATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                goToLoginPage();
            }
        }
    }

    private void goToLoginPage() {
        Intent intent = new Intent();
        setResult(-2, intent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int GALLERY_ACTIVITY_REQUEST_CODE = 3;
        int WISHLIST_ACTIVITY_REQUEST_CODE = 5;
        int HISTORY_ACTIVITY_REQUEST_CODE = 6;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.gallery:
                intent = new Intent(AccountSettingsActivity.this, GalleryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.cart:
                intent = new Intent(AccountSettingsActivity.this, CartActivity.class);
                intent.putExtra(getString(R.string.cart), id);
                //noinspection deprecation
                startActivityForResult(intent, CART_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.wishlist:
                intent = new Intent(AccountSettingsActivity.this, WishlistActivity.class);
                intent.putExtra(getString(R.string.wishlist), id);
                //noinspection deprecation
                startActivityForResult(intent, WISHLIST_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.history:
                intent = new Intent(AccountSettingsActivity.this, HistoryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, HISTORY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.logOut:
                intent = new Intent(AccountSettingsActivity.this, SureActivity.class);
                intent.putExtra(getString(R.string.yes), id);
                //noinspection deprecation
                startActivityForResult(intent, LOG_OUT_CONFIRMATION_REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

}
