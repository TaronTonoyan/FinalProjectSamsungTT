package com.samsung.gamingshop.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.gamingshop.R;

public class AdminActivity extends AppCompatActivity {

    private final int ADD_PRODUCT_REQUEST_CODE = 1;
    private final int LOG_OUT_CONFIRMATION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Admin");
        Button addAdmins = findViewById(R.id.addAdmins);
        Button addProduct = findViewById(R.id.addProduct);
        Button updateProduct = findViewById(R.id.updateProduct);
        Button deleteProduct = findViewById(R.id.deleteProduct);
        Button logOut = findViewById(R.id.logout);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        boolean isRoot = getIntent().getBooleanExtra(getString(R.string.is_root), false);

        Toast.makeText(getApplicationContext(), getString(R.string.admin_login), Toast.LENGTH_SHORT).show();

        if (!isRoot) {
            addAdmins.setVisibility(View.GONE);
        }

        addAdmins.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(AdminActivity.this, AddAdminsActivity.class);
            startActivity(intent);
        });
        addProduct.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(AdminActivity.this, ProductCategoryActivity.class);
            //noinspection deprecation
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
        });
        updateProduct.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(AdminActivity.this, GalleryActivity.class);
            startActivity(intent);
        });
        deleteProduct.setOnClickListener(v -> {
            mediaPlayer.start();
            long id = -2;
            Intent intent = new Intent(AdminActivity.this, GalleryActivity.class);
            intent.putExtra(getString(R.string.account), id);
            startActivity(intent);
        });
        logOut.setOnClickListener(v -> {
            mediaPlayer.start();
            Intent intent = new Intent(AdminActivity.this, SureActivity.class);
            long a = 0;
            intent.putExtra(getString(R.string.yes), a);
            //noinspection deprecation
            startActivityForResult(intent, LOG_OUT_CONFIRMATION_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRODUCT_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.product_added), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == LOG_OUT_CONFIRMATION_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

}
