package com.samsung.gamingshop.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.samsung.gamingshop.DBShop;
import com.samsung.gamingshop.R;
import com.samsung.gamingshop.models.Account;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private Account acc;
    private boolean remember;
    private SharedPreferences.Editor editor;
    private TextView text;

    private final int LOGIN_ACTIVITY_REQUEST_CODE = 1;
    private final int ADMIN_ACTIVITY_REQUEST_CODE = 2;
    private final int CART_ACTIVITY_REQUEST_CODE = 3;
    private final int ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE = 4;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 5;
    private final int GALLERY_ACTIVITY_REQUEST_CODE = 6;
    private final int WISHLIST_ACTIVITY_REQUEST_CODE = 7;
    private final int HISTORY_ACTIVITY_REQUEST_CODE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Menu");
        text = findViewById(R.id.text);
        Button gallery = findViewById(R.id.gallery);
        Button cart = findViewById(R.id.cart);
        Button wishlist = findViewById(R.id.wishlist);
        Button accountSettings = findViewById(R.id.accountSettings);
        Button history = findViewById(R.id.history);
        Button logOut = findViewById(R.id.logOut);
        DBConnector = new DBShop(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        rememberLogin();
        if (acc == null) {
            goToLoginPage();
        }

        gallery.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            intent.putExtra(getString(R.string.account), acc.getId());
            //noinspection deprecation
            startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
        });
        cart.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra(getString(R.string.cart), acc.getId());
            //noinspection deprecation
            startActivityForResult(intent, CART_ACTIVITY_REQUEST_CODE);
        });
        wishlist.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            intent.putExtra(getString(R.string.wishlist), acc.getId());
            //noinspection deprecation
            startActivityForResult(intent, WISHLIST_ACTIVITY_REQUEST_CODE);
        });
        history.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra(getString(R.string.account), acc.getId());
            //noinspection deprecation
            startActivityForResult(intent, HISTORY_ACTIVITY_REQUEST_CODE);
        });
        accountSettings.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(MainActivity.this, AccountSettingsActivity.class);
            intent.putExtra(getString(R.string.account), acc.getId());
            //noinspection deprecation
            startActivityForResult(intent, ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE);
        });
        logOut.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(MainActivity.this, SureActivity.class);
            intent.putExtra(getString(R.string.yes), acc.getId());
            //noinspection deprecation
            startActivityForResult(intent, LOG_OUT_CONFIRMATION_REQUEST_CODE);
        });
    }

    private void rememberLogin() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.account), MODE_PRIVATE);
        editor = sharedPref.edit();
        long id = sharedPref.getLong(getString(R.string.account), -1);
        if (id != -1) {
            acc = DBConnector.selectAcc(id);
            getAccount(acc.getEmail(), acc.getPassword());
        }
    }

    private void getAccount(String email, String password) {
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail()) && password.equals(arr.get(i).getPassword())) {
                acc = arr.get(i);
            }
        }
        login();
    }

    private void login() {
        if (remember) {
            editor.putLong(getString(R.string.account), acc.getId());
            editor.apply();
        }
        if (acc.getEmail().equals(getString(R.string.root_email))) {
            Intent i = new Intent(MainActivity.this, AdminActivity.class);
            i.putExtra(getString(R.string.is_root), true);
            //noinspection deprecation
            startActivityForResult(i, ADMIN_ACTIVITY_REQUEST_CODE);
        } else if (acc.getIsAdmin() == 1){
            Intent i = new Intent(MainActivity.this, AdminActivity.class);
            //noinspection deprecation
            startActivityForResult(i, ADMIN_ACTIVITY_REQUEST_CODE);
        } else {
            text.setText(acc.getEmail());
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            intent.putExtra(getString(R.string.account), acc.getId());
            //noinspection deprecation
            startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
        }
    }

    private void goToLoginPage() {
        editor.putLong(getString(R.string.account), -1);
        editor.apply();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        //noinspection deprecation
        startActivityForResult(i, LOGIN_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -2) {
            goToLoginPage();
        }
        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                String accEmail = data.getStringExtra(getString(R.string.email));
                String accPassword = data.getStringExtra(getString(R.string.password));
                remember = data.getBooleanExtra(getString(R.string.account), false);
                getAccount(accEmail, accPassword);
            } else {
                finish();
            }
        }
        if (requestCode == ADMIN_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                acc = null;
                goToLoginPage();
            } else {
                finish();
            }
        }
        if (requestCode == CART_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.order_delivered), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                goToLoginPage();
            }
        }
        if (requestCode == LOG_OUT_CONFIRMATION_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                goToLoginPage();
            }
        }
    }
}