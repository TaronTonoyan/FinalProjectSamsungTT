package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.R;

public class ProductCategoryActivity extends AppCompatActivity {

    private final int ADD_PRODUCT_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Choose Category");
        Button addConsole = findViewById(R.id.addConsole);
        Button addAccessory = findViewById(R.id.addAccessory);
        Button addGame = findViewById(R.id.addGame);
        Button allCategories = findViewById(R.id.allCategories);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        boolean isSort = getIntent().getBooleanExtra(getString(R.string.sort), false);
        boolean isUpdate = getIntent().getBooleanExtra(getString(R.string.category), false);

        if (isSort) {
            allCategories.setVisibility(View.VISIBLE);
            addConsole.setText(getString(R.string.console));
            addAccessory.setText(getString(R.string.accessory));
            addGame.setText(getString(R.string.game));
            addConsole.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.category), 1);
                setResult(RESULT_OK, intent);
                finish();
            });
            addAccessory.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.category), 2);
                setResult(RESULT_OK, intent);
                finish();
            });
            addGame.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.category), 3);
                setResult(RESULT_OK, intent);
                finish();
            });
            allCategories.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.category), 0);
                setResult(RESULT_OK, intent);
                finish();
            });
        } else if (isUpdate) {
            allCategories.setVisibility(View.GONE);
            addConsole.setText(getString(R.string.console));
            addAccessory.setText(getString(R.string.accessory));
            addGame.setText(getString(R.string.game));
            addConsole.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.add_products_category), getString(R.string.console));
                setResult(RESULT_OK, intent);
                finish();
            });
            addAccessory.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.add_products_category), getString(R.string.accessory));
                setResult(RESULT_OK, intent);
                finish();
            });
            addGame.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.add_products_category), getString(R.string.game));
                setResult(RESULT_OK, intent);
                finish();
            });
        } else {
            allCategories.setVisibility(View.GONE);
            addConsole.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent(ProductCategoryActivity.this, AddProductActivity.class);
                intent.putExtra(getString(R.string.category), getString(R.string.console));
                //noinspection deprecation
                startActivityForResult(intent, ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
            });
            addAccessory.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent(ProductCategoryActivity.this, AddProductActivity.class);
                intent.putExtra(getString(R.string.category), getString(R.string.accessory));
                //noinspection deprecation
                startActivityForResult(intent, ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
            });
            addGame.setOnClickListener(v -> {
                mediaPlayer.start();
                Intent intent = new Intent(ProductCategoryActivity.this, AddProductActivity.class);
                intent.putExtra(getString(R.string.category), getString(R.string.game));
                //noinspection deprecation
                startActivityForResult(intent, ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRODUCT_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
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
