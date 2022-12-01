package com.samsung.finalprojectsamsungtt;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class AddAdminsActivity extends AppCompatActivity {

    DBTickets DBConnector;
    ListView list;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admins);

        list = findViewById(R.id.accountListView);
        back = findViewById(R.id.backAddAdmins);
        DBConnector = new DBTickets(this);
        AccountAdapter adapter = new AccountAdapter(this, getAccounts());
        list.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Account[] getAccounts() {
        ArrayList<Account> accountArr = DBConnector.selectAllAccounts();
        Account[] arr = new Account[accountArr.size()-1];
        
        for (int i = 0; i < accountArr.size()-1; i++) {
            arr[i] = accountArr.get(i+1);
        }

        return arr;

    }

}