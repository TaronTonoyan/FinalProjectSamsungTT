package com.samsung.finalprojectsamsungtt.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.adapters.CartAdapter;
import com.samsung.finalprojectsamsungtt.adapters.WishlistAdapter;
import com.samsung.finalprojectsamsungtt.models.Order;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        initViews();
    }

    private void initViews() {
        ImageView back = findViewById(R.id.backWishlist);
        list = findViewById(R.id.wishlistListView);
        id = getIntent().getLongExtra(getString(R.string.wishlist), -1);
        DBConnector = new DBShop(this);
        WishlistAdapter adapter = new WishlistAdapter(this, getWishlistOrders());
        list.setAdapter(adapter);

        back.setOnClickListener(v -> finish());
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
        for (int i = 0; i < orderArr.size(); i++) {
            if (orderArr.get(i).getOwner() == id && orderArr.get(i).getIsWishlist() == 1) {
                wishlistArr.add(orderArr.get(i));
            }
        }
        Order[] arr = new Order[wishlistArr.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = wishlistArr.get(i);
        }

        return arr;
    }

}
