package com.samsung.finalprojectsamsungtt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.adapters.GalleryAdapter;
import com.samsung.finalprojectsamsungtt.models.Product;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private ListView list;
    private GalleryAdapter adapter;
    private int sortCode;
    private long id;

    private final int SORT_REQUEST_CODE = 1;
    private final int CART_ACTIVITY_REQUEST_CODE = 2;
    private final int ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE = 3;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        list = findViewById(R.id.listView);
        Button cart = findViewById(R.id.cart);
        Button sort = findViewById(R.id.sort);
        DBConnector = new DBShop(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        id = getIntent().getLongExtra(getString(R.string.account), -1);
        sortCode = 0;
        cart.setVisibility(View.GONE);

        if (id > 0) {
            actionBar.setTitle("Gallery");
            cart.setVisibility(View.VISIBLE);
            cart.setOnClickListener(v -> {
                Intent intent = new Intent(GalleryActivity.this, CartActivity.class);
                intent.putExtra(getString(R.string.cart), id);
                //noinspection deprecation
                startActivityForResult(intent, CART_ACTIVITY_REQUEST_CODE);
            });
        } else if (id == -2){
            actionBar.setTitle("Delete Product");
        } else {
            actionBar.setTitle("Update Product");
        }
        sort.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(GalleryActivity.this, ProductCategoryActivity.class);
            intent.putExtra(getString(R.string.sort), true);
            //noinspection deprecation
            startActivityForResult(intent, SORT_REQUEST_CODE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new GalleryAdapter(this, getProducts(), id);
        list.setAdapter(adapter);
    }

    private Product[] getProducts() {
        ArrayList<Product> productArr = DBConnector.selectAllProducts();
        ArrayList<Product> sortedArr = new ArrayList<>();
        for (int i = 0; i < productArr.size(); i++) {
            switch (sortCode) {
                case 0:
                    sortedArr.add(productArr.get(i));
                    break;
                case 1:
                    if (productArr.get(i).getCategory().equals(getString(R.string.console))) {
                        sortedArr.add(productArr.get(i));
                    }
                    break;
                case 2:
                    if (productArr.get(i).getCategory().equals(getString(R.string.accessory))) {
                        sortedArr.add(productArr.get(i));
                    }
                    break;
                case 3:
                    if (productArr.get(i).getCategory().equals(getString(R.string.game))) {
                        sortedArr.add(productArr.get(i));
                    }
                    break;
            }
        }
        Product[] arr = new Product[sortedArr.size()];

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
        if (requestCode == SORT_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                sortCode = data.getIntExtra(getString(R.string.category), 0);
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
        int WISHLIST_ACTIVITY_REQUEST_CODE = 5;
        int HISTORY_ACTIVITY_REQUEST_CODE = 6;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.cart:
                intent = new Intent(GalleryActivity.this, CartActivity.class);
                intent.putExtra(getString(R.string.cart), id);
                //noinspection deprecation
                startActivityForResult(intent, CART_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.wishlist:
                intent = new Intent(GalleryActivity.this, WishlistActivity.class);
                intent.putExtra(getString(R.string.wishlist), id);
                //noinspection deprecation
                startActivityForResult(intent, WISHLIST_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.history:
                intent = new Intent(GalleryActivity.this, HistoryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, HISTORY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.accountSettings:
                intent = new Intent(GalleryActivity.this, AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.logOut:
                intent = new Intent(GalleryActivity.this, SureActivity.class);
                intent.putExtra(getString(R.string.yes), id);
                //noinspection deprecation
                startActivityForResult(intent, LOG_OUT_CONFIRMATION_REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.filter(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
