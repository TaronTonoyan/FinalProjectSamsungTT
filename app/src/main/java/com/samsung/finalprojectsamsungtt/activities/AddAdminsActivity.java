package com.samsung.finalprojectsamsungtt.activities;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.samsung.finalprojectsamsungtt.DBShop;
import com.samsung.finalprojectsamsungtt.R;
import com.samsung.finalprojectsamsungtt.adapters.AccountAdapter;
import com.samsung.finalprojectsamsungtt.models.Account;

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
        ListView list = findViewById(R.id.accountListView);
        ImageView back = findViewById(R.id.backAddAdmins);
        DBConnector = new DBShop(this);
        AccountAdapter adapter = new AccountAdapter(this, getAccounts());
        list.setAdapter(adapter);

        back.setOnClickListener(v -> finish());
    }

    private Account[] getAccounts() {
        ArrayList<Account> accountArr = DBConnector.selectAllAccounts();
        Account[] arr = new Account[accountArr.size()-1];
        
        for (int i = 0; i < arr.length; i++) {
            arr[i] = accountArr.get(i+1);
        }

        return arr;

    }

}