package com.samsung.finalprojectsamsungtt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.adapters.WishlistAdapter;
import com.samsung.finalprojectsamsungtt.models.Order;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;
    private int categoryCode;
    private ListView list;
    private Button category;
    private View loadingScreen;

    private final int CATEGORY_REQUEST_CODE = 1;
    private final int CART_ACTIVITY_REQUEST_CODE = 2;
    private final int ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE = 4;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        list = findViewById(R.id.listView);
        category = findViewById(R.id.category);
        loadingScreen = findViewById(R.id.loading_screen);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        id = getIntent().getLongExtra(getString(R.string.wishlist), -1);
        DBConnector = new DBShop(this);
        actionBar.setTitle("Wishlist");
        categoryCode = 0;

        category.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(WishlistActivity.this, ProductCategoryActivity.class);
            intent.putExtra(getString(R.string.sort), true);
            //noinspection deprecation
            startActivityForResult(intent, CATEGORY_REQUEST_CODE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingScreen.setVisibility(View.VISIBLE);
        new LoadProductsTask().execute();
    }

    private Order[] getWishlistOrders() {
        ArrayList<Order> orderArr = DBConnector.selectAllOrders();
        ArrayList<Order> wishlistArr = new ArrayList<>();
        ArrayList<Order> sortedArr = new ArrayList<>();
        for (int i = 0; i < orderArr.size(); i++) {
            if (orderArr.get(i).getOwner() == id && orderArr.get(i).getIsWishlist() == 1) {
                wishlistArr.add(orderArr.get(i));
            }
        }
        for (int i = 0; i < wishlistArr.size(); i++) {
            switch (categoryCode) {
                case 0:
                    category.setText(getString(R.string.category));
                    sortedArr.add(wishlistArr.get(i));
                    break;
                case 1:
                    category.setText(getString(R.string.console));
                    if (DBConnector.selectProduct(wishlistArr.get(i).getProduct()).getCategory().equals(getString(R.string.console))) {
                        sortedArr.add(wishlistArr.get(i));
                    }
                    break;
                case 2:
                    category.setText(getString(R.string.accessory));
                    if (DBConnector.selectProduct(wishlistArr.get(i).getProduct()).getCategory().equals(getString(R.string.accessory))) {
                        sortedArr.add(wishlistArr.get(i));
                    }
                    break;
                case 3:
                    category.setText(getString(R.string.game));
                    if (DBConnector.selectProduct(wishlistArr.get(i).getProduct()).getCategory().equals(getString(R.string.game))) {
                        sortedArr.add(wishlistArr.get(i));
                    }
                    break;
            }
        }
        Order[] arr = new Order[sortedArr.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = sortedArr.get(i);
        }

        return arr;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -2) {
            goToLoginPage();
        }
        if (requestCode == CATEGORY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                categoryCode = data.getIntExtra(getString(R.string.category), 0);
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
        int HISTORY_ACTIVITY_REQUEST_CODE = 6;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.gallery:
                intent = new Intent(WishlistActivity.this, GalleryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.cart:
                intent = new Intent(WishlistActivity.this, CartActivity.class);
                intent.putExtra(getString(R.string.cart), id);
                //noinspection deprecation
                startActivityForResult(intent, CART_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.history:
                intent = new Intent(WishlistActivity.this, HistoryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, HISTORY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.accountSettings:
                intent = new Intent(WishlistActivity.this, AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.logOut:
                intent = new Intent(WishlistActivity.this, SureActivity.class);
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

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class LoadProductsTask extends AsyncTask<Void, Void, Order[]> {
        @Override
        protected Order[] doInBackground(Void... voids) {
            // Load products in background thread
            return getWishlistOrders();
        }

        @Override
        protected void onPostExecute(Order[] orders) {
            // Update adapter with loaded products and hide loading screen
            WishlistAdapter adapter = new WishlistAdapter(WishlistActivity.this, orders);
            list.setAdapter(adapter);
            loadingScreen.setVisibility(View.GONE);
        }
    }

}