package com.samsung.finalprojectsamsungtt.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Order;
import com.samsung.finalprojectsamsungtt.models.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private Button cart;
    private Button wishlist;
    private long accId;
    private long productId;
    private long cartId;
    private long wishlistId;
    private Bitmap bitmap;
    private URL url;

    private final int CART_ACTIVITY_REQUEST_CODE = 2;
    private final int ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE = 5;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        ImageView image = findViewById(R.id.image);
        TextView name = findViewById(R.id.name);
        TextView price = findViewById(R.id.price);
        TextView description = findViewById(R.id.description);
        TextView category = findViewById(R.id.category);
        EditText quantity = findViewById(R.id.quantity);
        cart = findViewById(R.id.addToCart);
        wishlist = findViewById(R.id.addToWishlist);
        DBConnector = new DBShop(this);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        accId = getIntent().getLongExtra(getString(R.string.account), -1);
        productId = getIntent().getLongExtra(getString(R.string.product), -1);
        Product product = DBConnector.selectProduct(productId);
        actionBar.setTitle(DBConnector.selectProduct(productId).getName());

        name.setText(product.getName());
        price.setText(product.getPrice() + "$");
        description.setText(product.getDescription());
        category.setText(product.getCategory());
        quantity.setText("1");
        try {
            url = new URL(product.getImage());
            ImageThread thread = new ImageThread();
            thread.start();
            thread.join();
            image.setImageBitmap(bitmap);
        } catch (MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }

        if (existsInCart()) {
            cart.setText(getString(R.string.remove_from_cart));
        } else {
            cart.setText(getString(R.string.add_to_cart));
        }
        if (existsInWishlist()) {
            wishlist.setText(getString(R.string.remove_from_wishlist));
        } else {
            wishlist.setText(getString(R.string.add_to_wishlist));
        }

        cart.setOnClickListener(v -> {
            mediaPlayer.start();
            if (existsInCart()) {
                cart.setText(getString(R.string.add_to_cart));
                DBConnector.deleteOrder(cartId);
            } else {
                cart.setText(getString(R.string.remove_from_cart));
                DBConnector.insertOrder(accId, productId, 0, Integer.parseInt(quantity.getText().toString()));
            }
        });
        wishlist.setOnClickListener(v -> {
            mediaPlayer.start();
            if (existsInWishlist()) {
                wishlist.setText(getString(R.string.add_to_wishlist));
                DBConnector.deleteOrder(wishlistId);
            } else {
                wishlist.setText(getString(R.string.remove_from_wishlist));
                DBConnector.insertOrder(accId, productId, 1, 1);
            }
        });
    }

    private boolean existsInCart() {
        ArrayList<Order> arr = DBConnector.selectAllOrders();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getOwner() == accId && arr.get(i).getProduct() == productId && arr.get(i).getIsWishlist() == 0) {
                cartId = arr.get(i).getId();
                return true;
            }
        }
        return false;
    }

    private boolean existsInWishlist() {
        ArrayList<Order> arr = DBConnector.selectAllOrders();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getOwner() == accId && arr.get(i).getProduct() == productId && arr.get(i).getIsWishlist() == 1) {
                wishlistId = arr.get(i).getId();
                return true;
            }
        }
        return false;
    }

    class ImageThread extends Thread{
        @Override
        public void run(){
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        int HISTORY_ACTIVITY_REQUEST_CODE = 4;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.gallery:
                intent = new Intent(ProductActivity.this, GalleryActivity.class);
                intent.putExtra(getString(R.string.account), accId);
                //noinspection deprecation
                startActivityForResult(intent, GALLERY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.cart:
                intent = new Intent(ProductActivity.this, CartActivity.class);
                intent.putExtra(getString(R.string.cart), accId);
                //noinspection deprecation
                startActivityForResult(intent, CART_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.wishlist:
                intent = new Intent(ProductActivity.this, WishlistActivity.class);
                intent.putExtra(getString(R.string.wishlist), accId);
                //noinspection deprecation
                startActivityForResult(intent, WISHLIST_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.history:
                intent = new Intent(ProductActivity.this, HistoryActivity.class);
                intent.putExtra(getString(R.string.account), accId);
                //noinspection deprecation
                startActivityForResult(intent, HISTORY_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.accountSettings:
                intent = new Intent(ProductActivity.this, AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.account), accId);
                //noinspection deprecation
                startActivityForResult(intent, ACCOUNT_SETTINGS_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.logOut:
                intent = new Intent(ProductActivity.this, SureActivity.class);
                intent.putExtra(getString(R.string.yes), accId);
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
