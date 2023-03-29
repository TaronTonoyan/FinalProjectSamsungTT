package com.samsung.gamingshop.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.gamingshop.DBShop;
import com.samsung.gamingshop.R;
import com.samsung.gamingshop.adapters.CartAdapter;
import com.samsung.gamingshop.models.Order;
import com.samsung.gamingshop.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;
    private ListView list;
    private int categoryCode;
    private TextView totalPrice;
    private Button category;
    private View loadingScreen;

    private final int ORDER_REQUEST_CODE = 1;
    private final int CATEGORY_REQUEST_CODE = 2;
    private final int ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE = 4;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 5;

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
        category = findViewById(R.id.category);
        Button order = findViewById(R.id.order);
        DBConnector = new DBShop(this);
        loadingScreen = findViewById(R.id.loading_screen);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        id = getIntent().getLongExtra(getString(R.string.cart), -1);
        actionBar.setTitle("Cart");
        categoryCode = 0;

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
        category.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(CartActivity.this, ProductCategoryActivity.class);
            intent.putExtra(getString(R.string.sort), true);
            startActivityForResult(intent, CATEGORY_REQUEST_CODE);
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        loadingScreen.setVisibility(View.VISIBLE);
        new LoadProductsTask().execute();
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
            switch (categoryCode) {
                case 0:
                    category.setText(getString(R.string.category));
                    sortedArr.add(cartArr.get(i));
                    break;
                case 1:
                    category.setText(getString(R.string.console));
                    if (DBConnector.selectProduct(cartArr.get(i).getProduct()).getCategory().equals(getString(R.string.console))) {
                        sortedArr.add(cartArr.get(i));
                    }
                    break;
                case 2:
                    category.setText(getString(R.string.accessory));
                    if (DBConnector.selectProduct(cartArr.get(i).getProduct()).getCategory().equals(getString(R.string.accessory))) {
                        sortedArr.add(cartArr.get(i));
                    }
                    break;
                case 3:
                    category.setText(getString(R.string.game));
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
        if (resultCode == -2) {
            goToLoginPage();
        }
        if (requestCode == ORDER_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        if (requestCode == CATEGORY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                categoryCode = data.getIntExtra(getString(R.string.category), 0);
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
        int WISHLIST_ACTIVITY_REQUEST_CODE = 6;
        int HISTORY_ACTIVITY_REQUEST_CODE = 7;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.gallery:
                intent = new Intent(CartActivity.this, GalleryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.wishlist:
                intent = new Intent(CartActivity.this, WishlistActivity.class);
                intent.putExtra(getString(R.string.wishlist), id);
                //noinspection deprecation
                startActivityForResult(intent, WISHLIST_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.history:
                intent = new Intent(CartActivity.this, HistoryActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, HISTORY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.accountSettings:
                intent = new Intent(CartActivity.this, AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.account), id);
                //noinspection deprecation
                startActivityForResult(intent, ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.logOut:
                intent = new Intent(CartActivity.this, SureActivity.class);
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

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class LoadProductsTask extends AsyncTask<Void, Void, Order[]> {
        @Override
        protected Order[] doInBackground(Void... voids) {
            // Load products in background thread
            return getSortedCartOrders();
        }

        @Override
        protected void onPostExecute(Order[] orders) {
            // Update adapter with loaded products and hide loading screen
            CartAdapter adapter = new CartAdapter(CartActivity.this, orders);
            list.setAdapter(adapter);
            loadingScreen.setVisibility(View.GONE);
        }
    }

}
