package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Product;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initViews();
    }

    private void initViews() {
        ImageView back = findViewById(R.id.backAddProduct);
        name = findViewById(R.id.productName);
        price = findViewById(R.id.productPrice);
        description = findViewById(R.id.productDescription);
        imageLink = findViewById(R.id.imageLink);
        image = findViewById(R.id.addProductImage);
        Button view = findViewById(R.id.viewImage);
        Button save = findViewById(R.id.saveProduct);
        addCategory = findViewById(R.id.addCategory);
        DBConnector = new DBShop(this);
        category = getIntent().getStringExtra(getString(R.string.category));
        addCategory.setText(category);
        long id = getIntent().getLongExtra(getString(R.string.product), -1);

        if (id == -1) {
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

        back.setOnClickListener(v -> finish());
        view.setOnClickListener(v -> {
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
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
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

}
