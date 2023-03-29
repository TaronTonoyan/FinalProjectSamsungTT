package com.samsung.gamingshop.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.gamingshop.DBShop;
import com.samsung.gamingshop.R;
import com.samsung.gamingshop.models.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AddProductActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private EditText name;
    private EditText price;
    private String category;
    private EditText description;
    private EditText imageLink;
    private ImageView image;
    private URL url;
    private Bitmap bitmap;
    private Product product;
    private Button addCategory;

    private final int ADD_CATEGORY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        imageLink = findViewById(R.id.imageUrl);
        image = findViewById(R.id.image);
        Button view = findViewById(R.id.view);
        Button save = findViewById(R.id.save);
        addCategory = findViewById(R.id.category);
        DBConnector = new DBShop(this);
        category = getIntent().getStringExtra(getString(R.string.category));
        addCategory.setText(category);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        long id = getIntent().getLongExtra(getString(R.string.product), -1);

        if (id == -1) {
            actionBar.setTitle("Add a Product");
            addCategory.setVisibility(View.GONE);
            save.setOnClickListener(v -> {
                try {
                    url = new URL(imageLink.getText().toString());
                    if (name.getText().toString().equals("") || price.getText().toString().equals("") || description.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.fill_form), Toast.LENGTH_SHORT).show();
                    } else {
                        DBConnector.insertProduct(name.getText().toString(), Float.parseFloat(price.getText().toString()), category, description.getText().toString(), imageLink.getText().toString());
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } catch (MalformedURLException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.not_url), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            actionBar.setTitle("Update your Product");
            addCategory.setVisibility(View.VISIBLE);
            product = DBConnector.selectProduct(id);
            name.setText(product.getName());
            price.setText(product.getPrice() + "");
            description.setText(product.getDescription());
            imageLink.setText(product.getImage());
            addCategory.setText(product.getCategory());
            save.setOnClickListener(v -> {
                try {
                    url = new URL(imageLink.getText().toString());
                    if (name.getText().toString().equals("") || price.getText().toString().equals("") || description.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.fill_form), Toast.LENGTH_SHORT).show();
                    } else {
                        product.setName(name.getText().toString());
                        product.setPrice(Float.parseFloat(price.getText().toString()));
                        product.setDescription(description.getText().toString());
                        product.setImage(imageLink.getText().toString());
                        DBConnector.updateProduct(product);
                        finish();
                    }
                } catch (MalformedURLException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.not_url), Toast.LENGTH_SHORT).show();
                }
            });
        }

        view.setOnClickListener(v -> {
            mediaPlayer.start();
            try {
                url = new URL(imageLink.getText().toString());
                ImageThread thread = new ImageThread();
                thread.start();
                thread.join();
                image.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.not_url), Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        addCategory.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductActivity.this, ProductCategoryActivity.class);
            intent.putExtra(getString(R.string.category), true);
            //noinspection deprecation
            startActivityForResult(intent, ADD_CATEGORY_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CATEGORY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                category = data.getStringExtra(getString(R.string.add_products_category));
                addCategory.setText(category);
                product.setCategory(category);
            }
        }
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
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
