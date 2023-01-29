package com.samsung.finalprojectsamsungtt.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
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
        accId = getIntent().getLongExtra(getString(R.string.account), -1);
        productId = getIntent().getLongExtra(getString(R.string.product), -1);
        Product product = DBConnector.selectProduct(productId);
        actionBar.setTitle(DBConnector.selectAcc(accId).getEmail());

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
            if (existsInCart()) {
                cart.setText(getString(R.string.add_to_cart));
                DBConnector.deleteOrder(cartId);
            } else {
                cart.setText(getString(R.string.remove_from_cart));
                DBConnector.insertOrder(accId, productId, 0, Integer.parseInt(quantity.getText().toString()));
            }
        });
        wishlist.setOnClickListener(v -> {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
