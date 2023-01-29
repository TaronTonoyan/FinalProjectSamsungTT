package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

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
    private int sortCode;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        list = findViewById(R.id.listView);
        Button cart = findViewById(R.id.cart);
        Button sort = findViewById(R.id.sort);
        id = getIntent().getLongExtra(getString(R.string.wishlist), -1);
        DBConnector = new DBShop(this);
        actionBar.setTitle(DBConnector.selectAcc(id).getEmail());
        sortCode = 0;

        cart.setOnClickListener(v -> {
            Intent intent = new Intent(WishlistActivity.this, CartActivity.class);
            intent.putExtra(getString(R.string.cart), id);
            startActivityForResult(intent, 3);
        });
        sort.setOnClickListener(v -> {
            Intent intent = new Intent(WishlistActivity.this, ProductCategoryActivity.class);
            intent.putExtra(getString(R.string.sort), true);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        WishlistAdapter adapter = new WishlistAdapter(this, getWishlistOrders());
        list.setAdapter(adapter);
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
            switch (sortCode) {
                case 0:
                    sortedArr.add(wishlistArr.get(i));
                    break;
                case 1:
                    if (DBConnector.selectProduct(wishlistArr.get(i).getProduct()).getCategory().equals(getString(R.string.console))) {
                        sortedArr.add(wishlistArr.get(i));
                    }
                    break;
                case 2:
                    if (DBConnector.selectProduct(wishlistArr.get(i).getProduct()).getCategory().equals(getString(R.string.accessory))) {
                        sortedArr.add(wishlistArr.get(i));
                    }
                    break;
                case 3:
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
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                sortCode = data.getIntExtra(getString(R.string.category), 0);
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
