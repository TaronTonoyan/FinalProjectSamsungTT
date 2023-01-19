package com.samsung.finalprojectsamsungtt.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private Account acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToLoginPage();
        initViews();
    }

    private void initViews() {
        Button gallery = findViewById(R.id.gallery);
        Button cart = findViewById(R.id.cart);
        Button wishlist = findViewById(R.id.wishlist);
        Button accountSettings = findViewById(R.id.accountSettings);
        Button logOut = findViewById(R.id.logOut);
        DBConnector = new DBShop(this);
        DBConnector.deleteOrder(66);

        gallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            intent.putExtra(getString(R.string.account), acc.getId());
            startActivity(intent);
        });
        cart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra(getString(R.string.cart), acc.getId());
            startActivityForResult(intent, 3);
        });
        wishlist.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            intent.putExtra(getString(R.string.wishlist), acc.getId());
            startActivity(intent);
        });
        accountSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountSettingsActivity.class);
            intent.putExtra(getString(R.string.account), acc.getId());
            startActivityForResult(intent, 4);
        });
        logOut.setOnClickListener(v -> goToLoginPage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                String accEmail = data.getStringExtra(getString(R.string.email));
                String accPassword = data.getStringExtra(getString(R.string.password));
                login(accEmail, accPassword);
            } else {
                finish();
            }
        }
        if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                acc = null;
                goToLoginPage();
            } else {
                finish();
            }
        }
        if (requestCode == 3){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.order_delivered), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 4){
            if (resultCode == RESULT_OK) {
                goToLoginPage();
            }
        }
    }

    private void login(String email, String password) {
        ArrayList<Account> arr = DBConnector.selectAllAccounts();
        for (int i = 0; i < arr.size(); i++) {
            if (email.equals(arr.get(i).getEmail()) && password.equals(arr.get(i).getPassword())) {
                acc = arr.get(i);
            }
        }
        if (acc.getEmail().equals(getString(R.string.root_email))) {
            Intent i = new Intent(MainActivity.this, AdminActivity.class);
            i.putExtra(getString(R.string.is_root), true);
            startActivityForResult(i, 2);
        } else if (acc.getIsAdmin() == 1){
            Intent i = new Intent(MainActivity.this, AdminActivity.class);
            startActivityForResult(i, 2);
        }
    }

    private void goToLoginPage() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(i, 1);
    }

}