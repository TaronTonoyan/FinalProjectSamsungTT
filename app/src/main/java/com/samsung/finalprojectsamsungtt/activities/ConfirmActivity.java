package com.samsung.finalprojectsamsungtt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.models.Account;
import com.samsung.finalprojectsamsungtt.models.Order;

import java.util.ArrayList;

public class ConfirmActivity extends AppCompatActivity {

    private DBShop DBConnector;
    private long id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        initViews();
    }

    private void initViews() {
        ImageView back = findViewById(R.id.backConfirm);
        EditText address = findViewById(R.id.confirmAddress);
        Button confirm = findViewById(R.id.confirm);
        DBConnector = new DBShop(this);
        id = getIntent().getLongExtra(getString(R.string.account), -1);
        Account acc = DBConnector.selectAcc(id);
        if (acc.getAddress() == null) {
            address.setHint(getString(R.string.no_address));
        } else {
            address.setText(acc.getAddress());
        }

        back.setOnClickListener(v -> finish());
        confirm.setOnClickListener(v -> {
            deleteCartOrders();
            setResult(RESULT_OK);
            finish();
        });
    }

    private void deleteCartOrders() {
        ArrayList<Order> arr = DBConnector.selectAllOrders();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getOwner() == id && arr.get(i).getIsWishlist() == 0) {
                DBConnector.deleteOrder(arr.get(i).getId());
            }
        }
    }

}
