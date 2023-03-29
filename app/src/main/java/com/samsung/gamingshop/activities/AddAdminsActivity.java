package com.samsung.gamingshop.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.samsung.gamingshop.DBShop;
import com.samsung.gamingshop.R;
import com.samsung.gamingshop.adapters.AccountAdapter;
import com.samsung.gamingshop.models.Account;

import java.util.ArrayList;


public class AddAdminsActivity extends AppCompatActivity {

    private DBShop DBConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admins);
        initViews();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Admin");
        ListView list = findViewById(R.id.listView);
        DBConnector = new DBShop(this);
        AccountAdapter adapter = new AccountAdapter(this, getAccounts());
        list.setAdapter(adapter);
    }

    private Account[] getAccounts() {
        ArrayList<Account> accountArr = DBConnector.selectAllAccounts();
        Account[] arr = new Account[accountArr.size()-1];
        
        for (int i = 0; i < arr.length; i++) {
            arr[i] = accountArr.get(i+1);
        }

        return arr;

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