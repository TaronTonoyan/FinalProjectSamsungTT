package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.adapters.GalleryAdapter;
import com.samsung.finalprojectsamsungtt.models.Product;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private DBShop DBConnector;
    ListView list;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initViews();
    }

    private void initViews() {
        list = findViewById(R.id.galleryListView);
        ImageView back = findViewById(R.id.backGallery);
        Button sort = findViewById(R.id.sort);
        DBConnector = new DBShop(this);
        id = getIntent().getLongExtra(getString(R.string.account), -1);

        back.setOnClickListener(v -> finish());
        sort.setOnClickListener(v -> {
            Intent intent = new Intent(GalleryActivity.this, ProductCategoryActivity.class);
            intent.putExtra(getString(R.string.sort), true);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GalleryAdapter adapter = new GalleryAdapter(this, getProducts(), id);
        list.setAdapter(adapter);
    }

    private Product[] getProducts() {
        ArrayList<Product> productArr = DBConnector.selectAllProducts();
        Product[] arr = new Product[productArr.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = productArr.get(i);
        }

        return arr;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                int sortCode = data.getIntExtra(getString(R.string.category), 0);
                sort(sortCode);
            }
        }
    }

    private void sort(int sortCode) {
        ArrayList<Product> productArr = DBConnector.selectAllProducts();
        ArrayList<Product> sortedArr = new ArrayList<>();
        for (int i = 0; i < productArr.size(); i++) {
            if (sortCode == 0 || (sortCode == 1 && productArr.get(i).getCategory().equals(getString(R.string.console))) || (sortCode == 2 && productArr.get(i).getCategory().equals(getString(R.string.accessory))) || (sortCode == 3 && productArr.get(i).getCategory().equals(getString(R.string.game)))) {
                sortedArr.add(productArr.get(i));
            }
        }
        Product[] arr = new Product[sortedArr.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = sortedArr.get(i);
        }
        GalleryAdapter adapter = new GalleryAdapter(this, arr, id);
        list.setAdapter(adapter);
    }

}
