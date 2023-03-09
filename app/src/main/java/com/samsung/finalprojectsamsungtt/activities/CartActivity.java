package com.samsung.finalprojectsamsungtt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.adapters.CartAdapter;
import com.samsung.finalprojectsamsungtt.models.Order;
import com.samsung.finalprojectsamsungtt.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;
    private ListView list;
    private int sortCode;
    private TextView totalPrice;

    private final int ORDER_REQUEST_CODE = 1;
    private final int SORT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initViews();
    }

    @SuppressWarnings("deprecation")
    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        totalPrice = findViewById(R.id.priceTotal);
        list = findViewById(R.id.listView);
        Button sort = findViewById(R.id.sort);
        Button order = findViewById(R.id.order);
        DBConnector = new DBShop(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        id = getIntent().getLongExtra(getString(R.string.cart), -1);
        actionBar.setTitle("Cart");
        sortCode = 0;

        order.setOnClickListener(v -> {
            mediaPlayer.start();
            if (getTotalPrice() > 0) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra(getString(R.string.account), id);
                intent.putExtra(getString(R.string.total_price), getTotalPrice());
                intent.putExtra(getString(R.string.order), historyOrders());
                startActivityForResult(intent, ORDER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();
            }
        });
        sort.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(CartActivity.this, ProductCategoryActivity.class);
            intent.putExtra(getString(R.string.sort), true);
            startActivityForResult(intent, SORT_REQUEST_CODE);
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        CartAdapter adapter = new CartAdapter(this, getSortedCartOrders());
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

    private Order[] getSortedCartOrders() {
        ArrayList<Order> orderArr = DBConnector.selectAllOrders();
        ArrayList<Order> cartArr = new ArrayList<>();
        ArrayList<Order> sortedArr = new ArrayList<>();
        for (int i = 0; i < orderArr.size(); i++) {
            if (orderArr.get(i).getOwner() == id && orderArr.get(i).getIsWishlist() == 0) {
                cartArr.add(orderArr.get(i));
            }
        }
        for (int i = 0; i < cartArr.size(); i++) {
            switch (sortCode) {
                case 0:
                    sortedArr.add(cartArr.get(i));
                    break;
                case 1:
                    if (DBConnector.selectProduct(cartArr.get(i).getProduct()).getCategory().equals(getString(R.string.console))) {
                        sortedArr.add(cartArr.get(i));
                    }
                    break;
                case 2:
                    if (DBConnector.selectProduct(cartArr.get(i).getProduct()).getCategory().equals(getString(R.string.accessory))) {
                        sortedArr.add(cartArr.get(i));
                    }
                    break;
                case 3:
                    if (DBConnector.selectProduct(cartArr.get(i).getProduct()).getCategory().equals(getString(R.string.game))) {
                        sortedArr.add(cartArr.get(i));
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

    private String historyOrders() {
        Order[] orderArr = getCartOrders();
        StringBuilder ans = new StringBuilder();
        for (Order order : orderArr) {
            Product product = DBConnector.selectProduct(order.getProduct());
            ans.append(order.getQuantity()).append(" ").append(product.getName()).append("\n");
        }
        
        return ans.toString();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ORDER_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        if (requestCode == SORT_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                sortCode = data.getIntExtra(getString(R.string.category), 0);
            }
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
