package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.R;

public class AdminActivity extends AppCompatActivity {

    private boolean isRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();
    }

    private void initViews() {
        Button addAdmins = findViewById(R.id.addAdmins);
        Button addProduct = findViewById(R.id.addProduct);
        Button updateProduct = findViewById(R.id.updateProduct);
        Button deleteProduct = findViewById(R.id.deleteProduct);
        Button logOut = findViewById(R.id.logout);
        isRoot = getIntent().getBooleanExtra(getString(R.string.is_root), false);

        Toast.makeText(getApplicationContext(), getString(R.string.admin_login), Toast.LENGTH_SHORT).show();

        if (!isRoot) {
            addAdmins.setVisibility(View.GONE);
        }

        addAdmins.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddAdminsActivity.class);
            startActivity(intent);
        });
        addProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ProductCategoryActivity.class);
            startActivityForResult(intent, 1);
        });
        updateProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, GalleryActivity.class);
            startActivity(intent);
        });
        deleteProduct.setOnClickListener(v -> {
            long id = -2;
            Intent intent = new Intent(AdminActivity.this, GalleryActivity.class);
            intent.putExtra(getString(R.string.account), id);
            startActivity(intent);
        });
        logOut.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.product_added), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
