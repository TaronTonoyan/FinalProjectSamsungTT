package com.samsung.finalprojectsamsungtt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.History;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;

    private final int CART_ACTIVITY_REQUEST_CODE = 2;
    private final int ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE = 4;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView text = findViewById(R.id.orders);
        id = getIntent().getLongExtra(getString(R.string.account), -1);
        DBConnector = new DBShop(this);
        actionBar.setTitle("History");
        text.setText(getHistory());
    }

    private String getHistory() {
        ArrayList<History> arr = DBConnector.selectAllHistory();
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getOwner() == id) {
                ans.append(arr.get(i).getAddress()).append(": ").append(arr.get(i).getPrice()).append("$\n").append(arr.get(i).getOrders());
            }
        }
        return ans.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -2) {
            goToLoginPage();
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
        int GALLERY_ACTIVITY_REQUEST_CODE = 1;
        int WISHLIST_ACTIVITY_REQUEST_CODE = 3;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.gallery:
                intent = new Intent(HistoryActivity.this, GalleryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.cart:
                intent = new Intent(HistoryActivity.this, CartActivity.class);
                intent.putExtra(getString(R.string.cart), id);
                //noinspection deprecation
                startActivityForResult(intent, CART_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.wishlist:
                intent = new Intent(HistoryActivity.this, WishlistActivity.class);
                intent.putExtra(getString(R.string.wishlist), id);
                //noinspection deprecation
                startActivityForResult(intent, WISHLIST_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.accountSettings:
                intent = new Intent(HistoryActivity.this, AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.logOut:
                intent = new Intent(HistoryActivity.this, SureActivity.class);
                intent.putExtra(getString(R.string.yes), id);
                //noinspection deprecation
                startActivityForResult(intent, LOG_OUT_CONFIRMATION_REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

}
