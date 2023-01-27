package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.adapters.CartAdapter;
import com.samsung.finalprojectsamsungtt.adapters.GalleryAdapter;
import com.samsung.finalprojectsamsungtt.models.Order;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;
    private ListView list;
    public static TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        totalPrice = findViewById(R.id.priceTotal);
        list = findViewById(R.id.cartListView);
        Button order = findViewById(R.id.order);
        DBConnector = new DBShop(this);
        id = getIntent().getLongExtra(getString(R.string.cart), -1);

        order.setOnClickListener(v -> {
            if (getTotalPrice() > 0) {
                Intent intent = new Intent(CartActivity.this, ConfirmActivity.class);
                intent.putExtra(getString(R.string.account), id);
                intent.putExtra(getString(R.string.total_price), getTotalPrice());
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CartAdapter adapter = new CartAdapter(this, getCartOrders());
        list.setAdapter(adapter);
        totalPrice.setText(getTotalPrice() + "$");
    }

    public float getTotalPrice() {
        float ans = 0;
        Order[] arr = getCartOrders();
        for (Order order : arr) {
            ans += DBConnector.selectProduct(order.getProduct()).getPrice() * order.getQuantity();
        }

        return ans;
    }

    private Order[] getCartOrders() {
        ArrayList<Order> orderArr = DBConnector.selectAllOrders();
        ArrayList<Order> cartArr = new ArrayList<>();
        for (int i = 0; i < orderArr.size(); i++) {
            if (orderArr.get(i).getOwner() == id && orderArr.get(i).getIsWishlist() == 0) {
                cartArr.add(orderArr.get(i));
            }
        }
        Order[] arr = new Order[cartArr.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = cartArr.get(i);
        }

        return arr;
    }

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
